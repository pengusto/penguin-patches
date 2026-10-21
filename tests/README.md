# Native description regression tests

Use an isolated emulator with the patched official Fossify Calendar 1.12.0 installed. The test APK must use the same temporary certificate as that installed APK. Never use a personal signing key for these tests.

Set `ANDROID_HOME`, Java 21 `JAVA_HOME`, `TEST_KEYSTORE` and `TEST_KEY_ALIAS`. The example uses the disposable Android test-key password `android`. From the repository root:

```sh
TOOLS="$ANDROID_HOME/build-tools/36.0.0"
API="$ANDROID_HOME/platforms/android-35/android.jar"
OUT=.scratch/regression
mkdir -p "$OUT/classes" "$OUT/dex"
"$JAVA_HOME/bin/javac" -cp "$API" -d "$OUT/classes" extensions/extension/src/main/java/io/github/pengusto/patches/extension/calendar/DescriptionPreview.java tests/DescriptionRegression.java tests/DescriptionInstrumentation.java
"$TOOLS/d8" --lib "$API" --output "$OUT/dex" "$OUT/classes/io/github/pengusto/patches/extension/calendar/DescriptionRegression.class" "$OUT/classes/io/github/pengusto/patches/extension/calendar/DescriptionInstrumentation.class"
"$TOOLS/aapt2" link -I "$API" --manifest tests/AndroidManifest.xml -o "$OUT/test.apk"
(cd "$OUT/dex" && zip -q ../test.apk classes.dex)
"$TOOLS/apksigner" sign --ks "$TEST_KEYSTORE" --ks-pass pass:android --ks-key-alias "$TEST_KEY_ALIAS" "$OUT/test.apk"
adb install -r "$OUT/test.apk"
adb shell am instrument -w io.github.pengusto.patches.calendar.tests/io.github.pengusto.patches.extension.calendar.DescriptionInstrumentation
```

Expected: `PASS: 9 native Android description regression cases`. Failure returns an assertion in the instrumentation result. Only the two test classes are bundled; rendering runs from the installed patched app. Android's HTML parser requires an Application context, so do not substitute a standalone dalvikvm/app_process run.

Also verify the actual day and Simple event list UI, empty-description visibility, exact source toggle, title-only save/readback, explicit source replacement and same-certificate updates. These integration checks are documented in `docs/VERIFICATION.md`; the nine parser tests alone do not prove correct bytecode hooks or sync behavior.

## Messages and File Manager instrumentation

`messages/` and `filemanager/` contain minimal Android Instrumentation classes and manifests. Compile with the Android 35 SDK and Java 21; use D8 and AAPT2 to package only the respective test classes (including FavoritePathsInstrumentation$Callback), not the runtime extension classes. Install the test APK with the same disposable test certificate as the patched target APK. Run:

```sh
adb shell am instrument -w io.github.pengusto.patches.messages.tests/io.github.pengusto.patches.tests.ReadNotificationsInstrumentation
adb shell am instrument -w io.github.pengusto.patches.filemanager.tests/io.github.pengusto.patches.tests.FavoritePathsInstrumentation
```

Both refuse physical-device execution. The File Manager tests temporarily replace the isolated app's favorite set, then restore it; use a fresh emulator without real user data. They use internal shared-storage fixtures and the target app's actual `renameFile` implementation. Messages needs notification permission. Keep Messages as default SMS app for the separate emulated-SMS UI integration test. Expected: 6 notification assertions and 26 favorite/path/callback/native-rename assertions. Android notification cancellation is asynchronous; the test uses a bounded state wait.

Also verify original vs patched UI with native screenshots, original-to-patched same-key update, read-state/provider retention, multiple UI renames, ordinary unfavorite vs file deletion, restart persistence and crash log. Exact reproduction and unverified storage/device gates are in docs/MESSAGES.md and docs/FILE_MANAGER.md.
