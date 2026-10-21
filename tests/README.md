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
