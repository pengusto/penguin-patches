package io.github.pengusto.patches.clock

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.*
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcodes
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.immutable.ImmutableDexFile
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodImplementation
import com.android.tools.smali.dexlib2.writer.io.MemoryDataStore
import com.android.tools.smali.dexlib2.writer.pool.DexPool
import java.security.MessageDigest

private const val context = "Landroid/content/Context;"
private const val alarm = "Lorg/fossify/clock/models/Alarm;"
private const val intent = "Landroid/content/Intent;"
private const val pending = "Landroid/app/PendingIntent;"
private const val runtime = "Lio/github/pengusto/patches/extension/clock/UpcomingAlarms;"
private const val hooks = "Lio/github/pengusto/patches/extension/clock/ClockHooks;"
private const val receivers = "Lorg/fossify/clock/receivers/"

private fun ClassDef.digest(): String {
    val store = MemoryDataStore()
    DexPool.writeTo(store, ImmutableDexFile(Opcodes.getDefault(), listOf(this)))
    return MessageDigest.getInstance("SHA-256").digest(store.data).joinToString("") { "%02x".format(it) }
}

@Suppress("unused")
val upcomingAlarmsPatch = bytecodePatch(
    name = "Choose upcoming alarm notification time",
    description = "Choose the advance notice in Morphe, with separate notifications and skip actions for each alarm occurrence. Keeps native ringing times and alarm definitions.",
    default = true,
) {
    compatibleWith(Compatibility(name = "Fossify Clock", packageName = "org.fossify.clock",
        targets = listOf(AppTarget(version = "1.6.0"))))
    extendWith("extensions/clock.mpe")
    val lead = intOption(
        key = "leadMinutes", default = 120,
        values = linkedMapOf("10 minutes" to 10, "30 minutes" to 30, "1 hour" to 60,
            "2 hours" to 120, "6 hours" to 360, "12 hours" to 720, "24 hours" to 1440),
        title = "Upcoming alarm notice (minutes)",
        description = "Minutes before each alarm, from 1 to 1440. Chosen when patching; patch the original APK again to change it.",
        required = true,
    )
    execute {
        // Desktop silently falls back to the default when an option validator rejects
        // a value. Validate here so an out-of-range request fails the patch instead.
        val leadMinutes = lead.value
        require(leadMinutes != null && leadMinutes in 1..1440) {
            "Invalid leadMinutes: choose a whole number from 1 to 1440."
        }
        fun MutableMethod.body(registers: Int, code: String) {
            val replacement = MutableMethod(ImmutableMethod(definingClass, name, parameters,
                returnType, accessFlags, annotations, hiddenApiRestrictions,
                ImmutableMethodImplementation(registers, emptyList(), emptyList(), emptyList())))
            replacement.addInstructions(0, code.trimIndent())
            mutableClassDefBy(definingClass).methods.apply {
                check(remove(this@body)) { "Clock binding method was not present: ${this@body}" }
                add(replacement)
            }
        }
        val hashes = mapOf(
            "Lj5/e;" to "310df744ab845554764a32472aea8a892440ebc702b960a8e42b85834da61954",
            "Lm5/d;" to "71ee7c6def87b5371bc8b0ad3f61adf14cb3ac3f2918b6ceae25b53f849c6279",
            "Lm5/h;" to "43b2974cbff60d10cee147ab1b3d68f5321afe0aab370a172cd783fad167de90",
            alarm to "570549d9957a67db7ce2a0a114968a87f5a7491552a4b8db41aa492c6caa1abb",
            "${receivers}AlarmReceiver;" to "8af07dcd2a005f99796a43b871a9f52bb93db6dce7abf10cd8ec6aad03240e08",
            "${receivers}RescheduleAlarmsReceiver;" to "1ce238b576d8cb7dc1c3a076bdb9103796d02ac2f7f7798030e253a400ee79ae",
            "${receivers}SkipUpcomingAlarmReceiver;" to "cf8b2b82d58f67f70f8a74123334b4da7201ee54826b67663ec81f3843b4df94",
            "${receivers}UpcomingAlarmReceiver;" to "61de79a7477e1a52e4497bba4ed369f0f17c33fb80f88ec54ab713e6d0adabf6",
        )
        hashes.forEach { (type, expected) ->
            check(classDefBy(type).digest() == expected) {
                "Unsupported or already patched Clock bytecode: $type. Use the original official 1.6.0 APK."
            }
        }
        fun method(owner: String, name: String, vararg parameters: String) =
            Fingerprint(definingClass = owner, name = name, parameters = parameters.toList()).method

        method(runtime, "leadMinutes").body(1, "const/16 v0, $leadMinutes\nreturn v0")
        method("Lj5/e;", "x", context, alarm, "J").body(4, """
            invoke-static {p0, p1, p2, p3}, $runtime->setup($context${alarm}J)V
            return-void
        """)
        method("Lj5/e;", "a", context, alarm).body(2, """
            invoke-static {p0, p1}, $runtime->cancel($context$alarm)V
            return-void
        """)
        method("Lj5/e;", "v", context, alarm).body(3, """
            invoke-virtual {p1}, $alarm->getId()I
            move-result v0
            invoke-static {p0, v0}, $runtime->upcoming(${context}I)$pending
            move-result-object v0
            return-object v0
        """)
        for ((name, skip) in listOf("UpcomingAlarmReceiver" to 0, "SkipUpcomingAlarmReceiver" to 1)) {
            method("$receivers$name;", "onReceive", context, intent).body(4, """
                const/4 v0, $skip
                invoke-static {p0, p1, p2, v0}, $runtime->receive(Landroid/content/BroadcastReceiver;$context${intent}Z)V
                return-void
            """)
        }
        method("${receivers}AlarmReceiver;", "onReceive", context, intent).addInstructions(0,
            "invoke-static {p1, p2}, $runtime->ringing($context$intent)V")
        method("${receivers}RescheduleAlarmsReceiver;", "onReceive", context, intent).addInstructions(0,
            "invoke-static {p1}, $runtime->migrate($context)V")

        // Bind concrete APIs only after validating the actual R8 classes above.
        method(hooks, "mainIntent", context, alarm).body(2, """
            invoke-static {p0, p1}, Lj5/e;->g($context$alarm)$pending
            move-result-object p0
            return-object p0
        """)
        method(hooks, "openIntent", context).body(1, """
            invoke-static {p0}, Lj5/e;->q($context)$pending
            move-result-object p0
            return-object p0
        """)
        method(hooks, "alarm", context, "I").body(3, """
            invoke-static {p0}, Lj5/e;->l($context)Lm5/h;
            move-result-object v0
            invoke-virtual {v0, p1}, Lm5/h;->i(I)$alarm
            move-result-object v0
            return-object v0
        """)
        for (name in listOf("a", "b")) {
            mutableClassDefBy("Lm5/d;").methods.single { it.name == name }.apply {
                accessFlags = (accessFlags and AccessFlags.PRIVATE.value.inv()) or AccessFlags.PUBLIC.value
            }
        }
        method(hooks, "finishOneTime", context, alarm).body(3, """
            invoke-static {p0}, Lj5/e;->f($context)Lm5/d;
            move-result-object v0
            invoke-virtual {v0, p1}, Lm5/d;->a($alarm)V
            return-void
        """)
        method(hooks, "refresh", context).body(1, """
            invoke-static {p0}, Lj5/e;->f($context)Lm5/d;
            move-result-object p0
            invoke-virtual {p0}, Lm5/d;->b()V
            return-void
        """)
        method(hooks, "error", context, "Ljava/lang/Exception;").body(2, """
            invoke-static {p0, p1}, Lf4/f;->Z(${context}Ljava/lang/Exception;)V
            return-void
        """)
        method(hooks, "is24Hour", context).body(1, """
            invoke-static {p0}, Lj5/e;->k($context)Lm5/e;
            move-result-object p0
            invoke-virtual {p0}, LJ5/c;->q()Z
            move-result p0
            return p0
        """)
    }
}
