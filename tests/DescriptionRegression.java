package io.github.pengusto.patches.extension.calendar;

/** Run on Android with the patched APK on CLASSPATH; no extra test libraries. */
public final class DescriptionRegression {
    private static void summary(String input, String expected) {
        String actual = DescriptionPreview.summary(input);
        if (!expected.equals(actual)) throw new AssertionError("Expected: " + expected + "; actual: " + actual);
    }

    public static void main(String[] args) {
        summary("", "");
        summary("Meet at 10 < 12 & bring notes", "Meet at 10 < 12 & bring notes");
        summary("Plain\nsecond line", "Plain\nsecond line");
        summary("<p>First</p><p>Second &amp; <b>bold</b></p>", "First Second & bold");
        summary("First<br>Second", "First Second");
        summary("<html><head><style>hidden</style></head><body><p>Visible</p><script>hidden</script></body></html>", "Visible");
        summary("<html><head><style>hidden</style></head><body></body></html>", "");
        summary("<p><a href=\"https://example.com\">Link</a></p>", "Link");
        summary("<p><a href=\"javascript:alert(1)\">Unsafe link</a></p>", "Unsafe link");
        System.out.println("PASS: 9 native Android description regression cases");
    }
}
