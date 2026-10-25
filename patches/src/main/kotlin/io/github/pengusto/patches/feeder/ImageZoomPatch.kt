package io.github.pengusto.patches.feeder

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.*
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcodes
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.immutable.*
import com.android.tools.smali.dexlib2.writer.io.MemoryDataStore
import com.android.tools.smali.dexlib2.writer.pool.DexPool
import java.security.MessageDigest

private const val native = "Lcom/nononsenseapps/feeder/ui/compose/html/LinearArticleContentKt;"
private const val model = "Lcom/nononsenseapps/feeder/model/html/LinearImage;"
private const val activity = "Lcom/nononsenseapps/feeder/ui/MainActivity;"
private const val runtime = "Lio/github/pengusto/patches/extension/feeder/ImageZoom;"
private const val hooks = "Lio/github/pengusto/patches/extension/feeder/FeederHooks;"
private const val continuation = "Lio/github/pengusto/patches/extension/feeder/ImageContinuation;"
private const val obj = "Ljava/lang/Object;"

private fun ClassDef.imageDigest(): String {
    val normalized = methods.map { m ->
        val code = m.implementation?.let {
            ImmutableMethodImplementation(it.registerCount, it.instructions, it.tryBlocks, emptyList())
        }
        ImmutableMethod(m.definingClass, m.name, m.parameters, m.returnType,
            m.accessFlags, m.annotations, m.hiddenApiRestrictions, code)
    }
    val out = MemoryDataStore()
    DexPool.writeTo(out, ImmutableDexFile(Opcodes.getDefault(), listOf(ImmutableClassDef(
        type, accessFlags, superclass, interfaces, sourceFile, annotations, fields, normalized))))
    return MessageDigest.getInstance("SHA-256").digest(out.data).joinToString("") { "%02x".format(it) }
}

@Suppress("unused")
val imageZoomPatch = bytecodePatch(
    name = "Zoom article images",
    description = "Open article images in a separate viewer with pinch, pan, double-tap and accessible zoom controls. Linked images offer View image or Open link. Uses Feeder's existing image loader and network policy. Official project F-Droid APK 2.25.0 only.",
    default = true,
) {
    compatibleWith(Compatibility(name = "Feeder", packageName = "com.nononsenseapps.feeder",
        targets = listOf(AppTarget(version = "2.25.0"))))
    extendWith("extensions/feeder.mpe")
    execute {
        mapOf(
            "Lb71;" to "90d6cd0b102e9cfb287c5c7adab247c41c815516ed56361adbcd9f0a38193710",
            "Lqp;" to "adf225cad79b1b876924047a7f1a3a0e4ab0df81070b25cf334a8b79f7902dcd",
            "Lc71;" to "eff7ba503c9f3f6f48a13c828709e9eba2e7a52a2a6b773f76c05e7034f6e708",
            "Lx13;" to "b7362e1449da18d0cf7e063bb439dc61cce243cb06c81ffa33fecfd6861db336",
            "Ljr2;" to "93257ad158d9558cea820c26341042415b418c6e45284ae0649c0e07a419e070",
            "Lbp1;" to "8ce23cb6d4f03a31091f0bb29e68bc4f4caadc691f12482458f7569760035167",
            "Lcom/nononsenseapps/feeder/FeederApplication;" to "da1dd0ba00571aa90d6b46221df78edda2bf632f89356c8b6fcb377c14765af9",
            "Lcom/nononsenseapps/feeder/model/html/LinearImage;" to "3b1853b378e32ddc8707086f06e1157fc8a55cd115cb3221c442c15b6dbf2793",
            "Lcom/nononsenseapps/feeder/model/html/LinearImageSource;" to "2f23cfa83f7ea642165223e6774ad269c9e400eddd61d1ce836b9190faf63c76",
            "Lcom/nononsenseapps/feeder/model/html/LinearText;" to "632ae15c2af9f72c53a988ecf29c23fb3998b9b41f07a35d91ac86dec5808627",
            "Lcom/nononsenseapps/feeder/ui/MainActivity;" to "6751305022aa801544efbde978ffde13ebce6c1955188436488a323ee65f6b10",
            "Lcom/nononsenseapps/feeder/ui/compose/html/LinearArticleContentKt;" to "cfdfaecb516c3b113ec54fbe64293ee768e87370007502f0b803ba944390936f",
            "Lcs2;" to "2dd89f629124434ec6d03cb019939632daf4587a952dea72a4ce1b9c5d822b4f",
            "Ld27;" to "2ebdf642ea96dff5ffbdc461aa0065a957261ef04df8ce9749cef9c9ba6b3922",
            "Lee0;" to "4efbe088ea7d2cb202dfbad0f56040b3e350119f2fe3cd5c034d6df643d852ae",
            "Lr13;" to "c2d7c021cb3140f5f150fba18e6b8ea8caa870a04be44088e47f90f8bf02e132",
            "Lgs2;" to "d587a06c5495bcf6dae268eb276bcd265b4c8acee61b39d62d20ce8d1b5a46ca",
            "Lgt6;" to "3361099bc418c27002133cebbd14ea2bd7c58c270c7b26826941d1efaf08adc7",
            "Lhu1;" to "37e92ead04cffb1f0b5e0f9e1386c0f2d710c7c72191eb7a5230bb808001f1ab",
            "Ls81;" to "ac557a9d46451f9e430f9973aad812323b1845573781eeb74b01cf35f1099f13",
            "Luv5;" to "dd34af1414e7a12c72b2e8e5df37a80ecfb6c955430d157088359db276bc15b6",
            "Luc2;" to "8e44621be2172ae85a47c353c0847898f80ecbed6a75c17f1b4ab8c550f84e76",
            "Lpk7;" to "1d0baad9c29a97754a22f43053b78373f4794885c3295148c3ce7364938aa021",
            "Lcom/nononsenseapps/feeder/ui/compose/feedarticle/ReaderViewKt;" to "14a9e7b940cce04f7cd9a835944082b4db72be23564352fa7b2ade677e46361c",
            "Lgm7;" to "47fcb9d504f55ed180d078a81e1bc9441bbc1f60d184c9a763a40c77bb416e50",
        ).forEach { (type, expected) ->
            check(classDefBy(type).imageDigest() == expected) {
                "Unsupported or already patched Feeder bytecode: $type. Use the original official project F-Droid 2.25.0 APK."
            }
        }
        fun method(owner: String, name: String): MutableMethod = mutableClassDefBy(owner).methods.single { it.name == name }
        fun bind(name: String, registers: Int, code: String) {
            val old = method(hooks, name)
            val replacement = MutableMethod(ImmutableMethod(hooks, old.name, old.parameters,
                old.returnType, old.accessFlags, old.annotations, old.hiddenApiRestrictions,
                ImmutableMethodImplementation(registers, emptyList(), emptyList(), emptyList())))
            replacement.addInstructions(0, code.trimIndent())
            mutableClassDefBy(hooks).methods.apply { check(remove(old)); add(replacement) }
        }
        method(activity, "onResume").addInstructions(0, "invoke-static {p0}, $runtime->bind(Landroid/app/Activity;)V")
        method(activity, "onStop").addInstructions(0, "invoke-static {p0}, $runtime->unbind(Landroid/app/Activity;)V")
        val click = method(native, "LinearImageContent\$lambda\$1\$0\$0\$0")
        val follow = MutableMethod(ImmutableMethod(native, "PenguinFollowImageLink", click.parameters,
            click.returnType, AccessFlags.PUBLIC.value or AccessFlags.STATIC.value,
            click.annotations, click.hiddenApiRestrictions, click.implementation))
        mutableClassDefBy(native).methods.add(follow)
        click.addInstructions(0, """
            invoke-static/range {p0 .. p2}, $runtime->click($obj$obj$obj)Z
            move-result v0
            if-eqz v0, :native_link
            sget-object v0, Lgm7;->a:Lgm7;
            return-object v0
            :native_link
            nop
        """.trimIndent())
        // v9 is the enabled argument of this exact guarded article-image clickable.
        method(native, "LinearImageContent\$lambda\$1\$0").addInstructions(48, "const/4 v9, 0x1")
        // At this guarded header-image AsyncImage call v3=request, v4=caption,
        // v5=modifier. The original parameter registers have already been reused.
        method("Lcom/nononsenseapps/feeder/ui/compose/feedarticle/ReaderViewKt;",
            "ReaderView\$lambda\$1\$0\$1\$3\$0\$0").addInstructions(92, """
            invoke-static {v3, v5, v4}, $runtime->header($obj${obj}Ljava/lang/String;)$obj
            move-result-object v5
            check-cast v5, Lex3;
        """.trimIndent())
        bind("requestUri", 1, """
            check-cast p0, Lgs2;
            iget-object p0, p0, Lgs2;->b:Ljava/lang/Object;
            check-cast p0, Ljava/lang/String;
            return-object p0
        """)
        bind("clickType", 1, "const-class v0, Luc2;\nreturn-object v0")
        bind("unit", 1, "sget-object v0, Lgm7;->a:Lgm7;\nreturn-object v0")
        bind("clickable", 9, """
            move-object v0, p0
            check-cast v0, Lex3;
            const/4 v1, 0x1
            move-object v2, p2
            const/4 v3, 0x0
            move-object v4, p1
            check-cast v4, Luc2;
            const/4 v5, 0x0
            invoke-static/range {v0 .. v5}, Lpk7;->e(Lex3;ZLjava/lang/String;Lz76;Luc2;I)Lex3;
            move-result-object v0
            return-object v0
        """)
        bind("sources", 1, """
            check-cast p0, $model
            invoke-virtual {p0}, $model->getSources()Ljava/util/List;
            move-result-object p0
            return-object p0
        """)
        bind("sourceUri", 1, """
            check-cast p0, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;
            invoke-virtual {p0}, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;->getImgUri()Ljava/lang/String;
            move-result-object p0
            return-object p0
        """)
        bind("sourceWidth", 2, """
            check-cast p0, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;
            invoke-virtual {p0}, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;->getScreenWidth()Ljava/lang/Integer;
            move-result-object v0
            if-nez v0, :value
            invoke-virtual {p0}, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;->getWidthPx()Ljava/lang/Integer;
            move-result-object v0
            if-nez v0, :value
            const/4 v0, 0x0
            return v0
            :value
            invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I
            move-result v0
            return v0
        """)
        bind("sourceDensity", 1, """
            check-cast p0, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;
            invoke-virtual {p0}, Lcom/nononsenseapps/feeder/model/html/LinearImageSource;->getPixelDensity()Ljava/lang/Float;
            move-result-object p0
            if-eqz p0, :none
            invoke-virtual {p0}, Ljava/lang/Float;->floatValue()F
            move-result p0
            return p0
            :none
            const/4 p0, 0x0
            return p0
        """)
        bind("link", 1, """
            check-cast p0, $model
            invoke-virtual {p0}, $model->getLink()Ljava/lang/String;
            move-result-object p0
            return-object p0
        """)
        bind("caption", 1, """
            check-cast p0, $model
            invoke-virtual {p0}, $model->getCaption()Lcom/nononsenseapps/feeder/model/html/LinearText;
            move-result-object p0
            if-eqz p0, :none
            invoke-virtual {p0}, Lcom/nononsenseapps/feeder/model/html/LinearText;->getText()Ljava/lang/String;
            move-result-object p0
            return-object p0
            :none
            const/4 p0, 0x0
            return-object p0
        """)
        bind("follow", 3, """
            check-cast p0, $model
            check-cast p1, Ljava/util/Map;
            check-cast p2, Ljd2;
            invoke-static {p0, p1, p2}, $native->PenguinFollowImageLink(${model}Ljava/util/Map;Ljd2;)Lgm7;
            return-void
        """)
        bind("job", 2, """
            new-instance v0, Lr13;
            const/4 v1, 0x0
            invoke-direct {v0, v1}, Lr13;-><init>(Lp13;)V
            return-object v0
        """)
        bind("cancel", 2, """
            check-cast p0, Lx13;
            const/4 v0, 0x0
            invoke-virtual {p0, v0}, Lx13;->i(Ljava/util/concurrent/CancellationException;)V
            return-void
        """)
        bind("continuation", 1, "const-class v0, Lb71;\nreturn-object v0")
        bind("suspended", 1, "sget-object v0, Ls81;->a:Ls81;\nreturn-object v0")
        val cont = mutableClassDefBy(continuation)
        cont.setSuperClass("Lc71;")
        val oldCtor = method(continuation, "<init>")
        val ctor = MutableMethod(ImmutableMethod(continuation, oldCtor.name, oldCtor.parameters,
            oldCtor.returnType, oldCtor.accessFlags, oldCtor.annotations, oldCtor.hiddenApiRestrictions,
            ImmutableMethodImplementation(2, emptyList(), emptyList(), emptyList())))
        ctor.addInstructions(0, """
            check-cast p1, Lb71;
            invoke-direct {p0, p1}, Lc71;-><init>(Lb71;)V
            return-void
        """.trimIndent())
        cont.methods.apply { check(remove(oldCtor)); add(ctor) }
        bind("execute", 7, """
            new-instance v0, Lcs2;
            invoke-direct {v0, p0}, Lcs2;-><init>(Landroid/content/Context;)V
            iput-object p1, v0, Lcs2;->c:Ljava/lang/Object;
            const/16 v1, 0x9c4
            invoke-static {v1, v1}, Lhu1;->e(II)Lgt6;
            move-result-object v1
            invoke-virtual {v0, v1}, Lcs2;->b(Lgt6;)V
            invoke-virtual {v0}, Lcs2;->a()Lgs2;
            move-result-object v0
            move-object v1, p0
            check-cast v1, Lcom/nononsenseapps/feeder/FeederApplication;
            invoke-virtual {v1, p0}, Lcom/nononsenseapps/feeder/FeederApplication;->newImageLoader(Landroid/content/Context;)Lrr2;
            move-result-object v1
            check-cast v1, Luv5;
            new-instance v2, $continuation
            invoke-direct {v2, p2}, $continuation-><init>($obj)V
            invoke-virtual {v1, v0, v2}, Luv5;->b(Lgs2;Lc71;)Ljava/lang/Object;
            move-result-object v0
            return-object v0
        """)
        bind("image", 2, """
            instance-of v0, p0, Ld27;
            if-eqz v0, :error
            check-cast p0, Ld27;
            invoke-virtual {p0}, Ld27;->getImage()Ljr2;
            move-result-object p0
            return-object p0
            :error
            const/4 p0, 0x0
            return-object p0
        """)
        for ((name, target) in mapOf("width" to "b", "height" to "a")) bind(name, 1, """
            check-cast p0, Ljr2;
            invoke-interface {p0}, Ljr2;->$target()I
            move-result p0
            return p0
        """)
        bind("draw", 2, """
            check-cast p0, Ljr2;
            invoke-interface {p0, p1}, Ljr2;->d(Landroid/graphics/Canvas;)V
            return-void
        """)
        bind("drawable", 4, """
            instance-of v0, p1, Lee0;
            if-eqz v0, :drawable
            check-cast p1, Lee0;
            iget-object p1, p1, Lee0;->a:Landroid/graphics/Bitmap;
            invoke-virtual {p0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;
            move-result-object p0
            new-instance v0, Landroid/graphics/drawable/BitmapDrawable;
            invoke-direct {v0, p0, p1}, Landroid/graphics/drawable/BitmapDrawable;-><init>(Landroid/content/res/Resources;Landroid/graphics/Bitmap;)V
            return-object v0
            :drawable
            instance-of v0, p1, Lbp1;
            if-eqz v0, :other
            check-cast p1, Lbp1;
            iget-object p1, p1, Lbp1;->a:Landroid/graphics/drawable/Drawable;
            return-object p1
            :other
            const/4 p1, 0x0
            return-object p1
        """)
    }
}
