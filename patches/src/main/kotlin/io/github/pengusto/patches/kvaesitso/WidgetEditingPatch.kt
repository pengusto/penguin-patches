package io.github.pengusto.patches.kvaesitso

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.*
import app.morphe.patcher.util.proxy.mutableTypes.MutableField
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcodes
import com.android.tools.smali.dexlib2.immutable.*
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.RegisterRangeInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rc
import com.android.tools.smali.dexlib2.writer.io.MemoryDataStore
import com.android.tools.smali.dexlib2.writer.pool.DexPool
import java.security.MessageDigest

private const val scaffold = "Lde/mm20/launcher2/ui/launcher/scaffold/"
private const val components = "${scaffold}components/"
private const val state = "${scaffold}LauncherScaffoldState;"
private const val widgets = "${components}WidgetsComponent;"
private const val settings = "Lde/mm20/launcher2/preferences/ui/UiSettings;"
private const val menu = "Lde/mm20/launcher2/ui/launcher/searchbar/SearchBarMenuKt;"
private const val bridge = "Lio/github/pengusto/patches/extension/kvaesitso/WidgetEditing;"
private const val mutableState = "Landroidx/compose/runtime/MutableState;"
private const val composer = "Landroidx/compose/runtime/Composer;"
private const val scope = "Lkotlinx/coroutines/CoroutineScope;"

// Canonical, separately serialized original classes. Covers registers, branches,
// fields and called APIs as well as the injection sites; reject modified inputs.
private fun ClassDef.digest(): String {
    val store = MemoryDataStore()
    DexPool.writeTo(store, ImmutableDexFile(Opcodes.getDefault(), listOf(this)))
    return MessageDigest.getInstance("SHA-256").digest(store.data).joinToString("") { "%02x".format(it) }
}

private fun getter(owner: String, name: String, type: String, body: String): MutableMethod =
    MutableMethod(ImmutableMethod(owner, name, emptyList(), type, AccessFlags.PUBLIC.value,
        emptySet(), emptySet(), ImmutableMethodImplementation(2, emptyList(), emptyList(), emptyList())))
        .apply { addInstructions(0, body) }

@Suppress("unused")
val widgetEditingPatch = bytecodePatch(
    name = "Restore widget editing from the search menu",
    description = "Open the existing widget editor from the search bar menu when the Edit button is hidden, without changing the preference or stored widgets.",
    default = true,
) {
    compatibleWith(Compatibility(name = "Kvaesitso", packageName = "de.mm20.launcher2.release",
        targets = listOf(AppTarget(version = "1.41.0"))))
    extendWith("extensions/kvaesitso.mpe")
    execute {
        val bindClass = "${scaffold}LauncherScaffoldKt\$\$ExternalSyntheticLambda10;"
        val hashes = mapOf(
            settings to "3eb6a05c1332e96332f22ed50b1cb98bd6836c2522baddf7b4d04dee18b8db65",
            bindClass to "9ef6c5af4d9e02eabd493e082455dfcecfcdff95753891073641e7646ef31229",
            "${scaffold}LauncherScaffoldKt;" to "5fef4045c8e7dc8adb9585eaf39338ffb4bad25e71e23cfbb79ab4456c006425",
            state to "a76adf16581a7fdc7c3a628669854a885090766c670a1160a9192ef8002f8719",
            "${components}ClockAndWidgetsHomeComponent;" to "6d046afa868a25e72aa4a14b450648e793c3bde4a0122b023e86e33bbc7787ff",
            widgets to "4b7c2652690a7a07f3209f43519f96c9788384608b408652f59b140961c51dc2",
            menu to "f1972bb7824ab622e9a6e06d8e7ad2a9640f276e063ccd1c78b0bdc180781c88",
        )
        hashes.forEach { (type, expected) ->
            check(classDefBy(type).digest() == expected) {
                "Unsupported or already patched Kvaesitso bytecode: $type. Use the original official 1.41.0 APK."
            }
        }

        // R8 moved this retained stdlib function to the facade's implementation
        // class and removed it from the facade hierarchy. Bind only this exact API.
        classDefForEach { cls ->
            if (cls.type.startsWith("Lio/github/pengusto/patches/extension/kvaesitso/")) {
                mutableClassDefBy(cls).methods.forEach { method ->
                    method.implementation?.instructions?.toList()?.forEachIndexed { index, instruction ->
                        val ref = (instruction as? ReferenceInstruction)?.reference as? MethodReference
                        if (ref?.definingClass == "Lkotlin/coroutines/intrinsics/IntrinsicsKt;" &&
                            ref.name == "getCOROUTINE_SUSPENDED" && ref.parameterTypes.isEmpty()) {
                            val target = ImmutableMethodReference("Lkotlin/coroutines/intrinsics/IntrinsicsKt__IntrinsicsKt;",
                                ref.name, emptyList(), ref.returnType)
                            when (instruction) {
                                is FiveRegisterInstruction -> method.replaceInstruction(index, BuilderInstruction35c(
                                    instruction.opcode, instruction.registerCount, instruction.registerC, instruction.registerD,
                                    instruction.registerE, instruction.registerF, instruction.registerG, target))
                                is RegisterRangeInstruction -> method.replaceInstruction(index, BuilderInstruction3rc(
                                    instruction.opcode, instruction.startRegister, instruction.registerCount, target))
                                else -> error("Unsupported coroutine intrinsic invocation")
                            }
                        }
                    }
                }
            }
        }

        val stateClass = mutableClassDefBy(state)
        stateClass.methods.add(getter(state, "getPenguinConfig", "${scaffold}ScaffoldConfiguration;", """
            iget-object v0, p0, $state->config:${scaffold}ScaffoldConfiguration;
            return-object v0
        """.trimIndent()))
        stateClass.methods.single { it.name == "performTapGesture" }.apply {
            accessFlags = (accessFlags and AccessFlags.PRIVATE.value.inv()) or AccessFlags.PUBLIC.value
        }
        mutableClassDefBy("${components}ClockAndWidgetsHomeComponent;").methods.single {
            it.name == "setEditMode"
        }.apply {
            accessFlags = (accessFlags and AccessFlags.PRIVATE.value.inv()) or AccessFlags.PUBLIC.value
        }

        val widgetClass = mutableClassDefBy(widgets)
        widgetClass.fields.add(MutableField(ImmutableField(widgets, "penguinEditRequest", mutableState,
            AccessFlags.PRIVATE.value or AccessFlags.FINAL.value, null, emptySet(), emptySet())))
        widgetClass.methods.add(getter(widgets, "getPenguinEditRequest", mutableState, """
            iget-object v0, p0, $widgets->penguinEditRequest:$mutableState
            return-object v0
        """.trimIndent()))
        val constructor = widgetClass.methods.single { it.name == "<init>" }
        constructor.addInstructions(constructor.implementation!!.instructions.lastIndex, """
            invoke-static {}, $bridge->newEditRequest()$mutableState
            move-result-object v0
            iput-object v0, p0, $widgets->penguinEditRequest:$mutableState
        """.trimIndent())

        val widgetUi = widgetClass.methods.single { it.name == "Component" }
        // Immediately after the existing rememberSaveable edit-mode state.
        widgetUi.addInstructions(96, """
            const/4 v10, 0x0
            invoke-static {v1, v0, v12, v10}, $bridge->ObserveEditRequest($widgets$mutableState${composer}I)V
        """.trimIndent())

        val bind = mutableClassDefBy(bindClass).methods.single { it.name == "<init>" }
        bind.addInstructions(bind.implementation!!.instructions.lastIndex,
            "invoke-static {p2, p1}, $bridge->bind($state$scope)V")
        val settingsConstructor = mutableClassDefBy(settings).methods.single { it.name == "<init>" }
        settingsConstructor.addInstructions(settingsConstructor.implementation!!.instructions.lastIndex,
            "invoke-static {p0}, $bridge->observeSettings($settings)V")

        val menuContent = Fingerprint(definingClass = menu, name = "SearchBarMenu\$lambda\$3\$3\$0",
            returnType = "Lkotlin/Unit;", parameters = listOf("Landroid/content/Context;", mutableState,
                "Landroidx/compose/foundation/layout/ColumnScope;", composer, "I")).method
        // Render within the existing menu group, before Wallpaper. Reuse its
        // localized resources and preserve Wallpaper, Settings and Help.
        menuContent.replaceInstruction(24,
            "invoke-static {v14, v3}, $bridge->WallpaperShape(${composer}I)Landroidx/compose/ui/graphics/Shape;")
        // Insert after the original branch target, otherwise the normal
        // non-tracing execution jumps over the new menu item.
        menuContent.addInstructions(23, """
            move-object/from16 v0, p1
            const/4 v3, 0x0
            invoke-static {v0, v14, v3}, $bridge->MenuItem($mutableState${composer}I)V
            move-object/from16 v0, p0
        """.trimIndent())

        // The upstream secondary-page set omits the Home button destination.
        // Include it so a widget page assigned only to that gesture can render.
        val secondary = mutableClassDefBy("${scaffold}LauncherScaffoldKt;").methods.single { it.name == "SecondaryPage" }
        secondary.replaceInstruction(150, "const/16 v15, 0x8")
        secondary.addInstructionsWithLabels(161, """
            invoke-virtual {v6}, ${scaffold}ScaffoldConfiguration;->getHomeButton()${scaffold}ScaffoldGesture;
            move-result-object v3
            if-eqz v3, :store_home_button
            invoke-virtual {v3}, ${scaffold}ScaffoldGesture;->getComponent()${components}ScaffoldComponent;
            move-result-object v3
            :store_home_button
            const/4 v8, 0x7
            aput-object v3, v15, v8
        """.trimIndent())
    }
}
