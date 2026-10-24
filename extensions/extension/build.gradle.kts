extension {
    name = "extensions/extension.mpe"
}

android {
    namespace = "io.github.pengusto.patches.extension"
    // Keep Feeder's Java-only runtime isolated from the released Fossify helpers.
    val commonSources = file("src/main/java/io/github/pengusto/patches/extension")
        .listFiles().orEmpty().filter { it.isDirectory && it.name != "feeder" }
    sourceSets.getByName("main").apply {
        java.setSrcDirs(commonSources)
        kotlin.setSrcDirs(commonSources)
    }
}
