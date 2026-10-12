# Pengusto Patches

- Independent GPL-3.0 patch collection compatible with Morphe; preserve LICENSE, NOTICE and source attribution.
- Put app patches under `patches/src/main/kotlin/io/github/pengusto/patches/<app>/` and runtime extensions under `extensions/extension/src/main/java/io/github/pengusto/patches/extension/<app>/`.
- Use official Morphe APIs and existing Gradle tooling. No embedded APKs, signing keys or tokens in Git.
- Work on dev. Publish only patches tested against specified original APK versions, including signature/package identity, install, UI and save/data regression checks. Document original APK provenance and checksum.
- Do not claim compatibility with untested versions or silently rewrite stored app data. Fail clearly on unsupported bytecode.
- Keep release workflow manual until the first patch is verified. Use the template semantic release pipeline; do not manually edit generated patch metadata.
- Local build: Java 21, Android SDK, `./gradlew :patches:buildAndroid`. GitHub Packages needs read:packages; pass credentials via environment, never store them in project files.
- Calendar's existing fork is the reference implementation, not an already converted Morphe patch. Preserve exact HTML on unrelated event edits.
