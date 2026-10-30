/* SPDX-License-Identifier: GPL-3.0-only */
import com.android.tools.smali.dexlib2.DexFileFactory;
import com.android.tools.smali.dexlib2.Opcodes;
import com.android.tools.smali.dexlib2.iface.ClassDef;
import com.android.tools.smali.dexlib2.iface.Method;
import com.android.tools.smali.dexlib2.iface.instruction.*;
import java.io.File;
import java.util.*;
import java.util.zip.ZipFile;
import kotlin.jvm.functions.Function1;

/** Verify the release changes exactly the pinned deletion fallback and its helper. */
class VerifyExport {
    private static final String OWNER = "Leu/kanade/tachiyomi/data/download/DownloadManager;";
    private static final String HELPER = "penguinDefaultCategory";
    private static final String FILTER = "getChaptersToDelete";
    private static final java.lang.reflect.Method DIGEST;
    static {
        try {
            DIGEST = Class.forName("io.github.pengusto.patches.CodeDigestKt")
                .getDeclaredMethod("codeDigest", ClassDef.class, Function1.class);
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    private static String digest(ClassDef type, boolean unchanged) throws Exception {
        Function1<Method, Boolean> include = method -> !unchanged ||
            (!method.getName().equals(FILTER) && !method.getName().equals(HELPER));
        return (String) DIGEST.invoke(null, type, include);
    }
    private static Map<String, ClassDef> classes(String path) throws Exception {
        var result = new TreeMap<String, ClassDef>();
        var apk = DexFileFactory.loadDexContainer(new File(path), Opcodes.getDefault());
        for (String entry : apk.getDexEntryNames())
            for (var type : apk.getEntry(entry).getDexFile().getClasses())
                if (result.put(type.getType(), type) != null) throw new AssertionError("Duplicate class");
        return result;
    }
    private static Method method(ClassDef type, String name) {
        var matches = new ArrayList<Method>();
        for (var m : type.getMethods()) if (m.getName().equals(name)) matches.add(m);
        if (matches.size() != 1) throw new AssertionError("Expected one " + name);
        return matches.get(0);
    }
    private static String instruction(Instruction i) {
        StringBuilder s = new StringBuilder(i.getOpcode().name());
        if (i instanceof OneRegisterInstruction r) s.append(" a=" + r.getRegisterA());
        if (i instanceof TwoRegisterInstruction r) s.append(" b=" + r.getRegisterB());
        if (i instanceof ThreeRegisterInstruction r) s.append(" c=" + r.getRegisterC());
        if (i instanceof FiveRegisterInstruction r) s.append(" n=" + r.getRegisterCount() + ":" + r.getRegisterC() + "," + r.getRegisterD() + "," + r.getRegisterE() + "," + r.getRegisterF() + "," + r.getRegisterG());
        if (i instanceof RegisterRangeInstruction r) s.append(" r=" + r.getStartRegister() + "+" + r.getRegisterCount());
        if (i instanceof WideLiteralInstruction r) s.append(" l=" + r.getWideLiteral());
        if (i instanceof OffsetInstruction r) s.append(" o=" + r.getCodeOffset());
        if (i instanceof ReferenceInstruction r) s.append(" ref=" + r.getReference());
        if (i instanceof DualReferenceInstruction r) s.append(" ref2=" + r.getReference2());
        return s.toString();
    }
    private static List<String> instructions(com.android.tools.smali.dexlib2.iface.MethodImplementation code) {
        var addresses = new HashMap<Integer, Integer>();
        int address = 0, index = 0;
        for (var i : code.getInstructions()) { addresses.put(address, index++); address += i.getCodeUnits(); }
        addresses.put(address, index);
        var result = new ArrayList<String>(); address = 0;
        for (var i : code.getInstructions()) {
            String text = instruction(i);
            if (i instanceof OffsetInstruction jump) {
                Integer target = addresses.get(address + jump.getCodeOffset());
                if (target == null) throw new AssertionError("Jump outside instruction boundaries");
                text = text.replace(" o=" + jump.getCodeOffset(), " target=" + target);
            }
            result.add(text); address += i.getCodeUnits();
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("Original APK, exported APK required");
        var before = classes(args[0]); var after = classes(args[1]);
        if (!before.keySet().equals(after.keySet())) throw new AssertionError("Added or removed app/library classes");
        for (var entry : before.entrySet()) {
            String name = entry.getKey();
            if (!digest(entry.getValue(), name.equals(OWNER)).equals(digest(after.get(name), name.equals(OWNER))))
                throw new AssertionError("Unrelated code changed: " + name);
        }
        if (!digest(before.get(OWNER), false).equals("5dd3e964a2130e4e0ca7bc40dad5167f8ecbf989d4ffc7a1ffe3e388ac3c4368"))
            throw new AssertionError("Unsupported original owner");
        var original = method(before.get(OWNER), FILTER).getImplementation();
        var patched = method(after.get(OWNER), FILTER).getImplementation();
        var a = instructions(original); var b = instructions(patched);
        if (a.size() != b.size() || original.getRegisterCount() != patched.getRegisterCount())
            throw new AssertionError("Unexpected filter layout");
        for (int i = 0; i < a.size(); i++)
            if ((i < 84 || i > 86) && !a.get(i).equals(b.get(i)))
                throw new AssertionError("Unrelated filter instruction changed at " + i + ": " + a.get(i) + " -> " + b.get(i));
        if (!original.getTryBlocks().isEmpty() || !patched.getTryBlocks().isEmpty())
            throw new AssertionError("Unexpected filter exception blocks");
        if (!b.get(85).equals("MOVE_RESULT_OBJECT a=11") || !b.get(86).equals("NOP"))
            throw new AssertionError("Wrong fallback result register");
        // Canonical comparison above protects every other method. Pin the new call and Long helper.
        var instructions = new ArrayList<com.android.tools.smali.dexlib2.iface.instruction.Instruction>();
        patched.getInstructions().forEach(instructions::add);
        if (!(instructions.get(84) instanceof ReferenceInstruction call) ||
            !call.getReference().toString().equals(OWNER + "->" + HELPER + "()Ljava/lang/Long;"))
            throw new AssertionError("Missing fallback helper call");
        var helper = method(after.get(OWNER), HELPER);
        if (!helper.getReturnType().equals("Ljava/lang/Long;") || helper.getImplementation().getRegisterCount() != 2)
            throw new AssertionError("Wrong Long helper");
        var h = new ArrayList<String>();
        helper.getImplementation().getInstructions().forEach(i -> h.add(instruction(i)));
        if (!h.equals(List.of("CONST_WIDE_16 a=0 l=0",
            "INVOKE_STATIC n=2:0,1,0,0,0 ref=Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;",
            "MOVE_RESULT_OBJECT a=0", "RETURN_OBJECT a=0"))) throw new AssertionError("Wrong helper code: " + h);
        try (var x = new ZipFile(args[0]); var y = new ZipFile(args[1])) {
            for (var e : Collections.list(x.entries())) {
                String n = e.getName();
                if (n.matches("classes[0-9]*\\.dex") || n.matches("META-INF/(MANIFEST.MF|[^/]+\\.(SF|RSA|DSA|EC))")) continue;
                if (y.getEntry(n) == null || !Arrays.equals(x.getInputStream(e).readAllBytes(), y.getInputStream(y.getEntry(n)).readAllBytes()))
                    throw new AssertionError("Unrelated resource/native entry changed: " + n);
            }
        }
        System.out.println("PASS: " + before.size() + " classes; only deletion fallback/helper changed; resources and native libraries retained");
    }
}
