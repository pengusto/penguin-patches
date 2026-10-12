# Calendar patch verification

Original APK: official Fossify Calendar 1.12.0, SHA-256 d43b2df05d5702bea760a7ab03402977ba72e446e5932038efbdab248a5de987.

Local MPP build: passed. Morphe Desktop 1.18.1 application and resource rebuild: passed. Android 15 ARM64 installation/start: passed. Native preview, exact original source toggle, title-only save/readback after process restart, explicit source edit/save/readback after restart: passed. Same-certificate APK update installation: passed. AndroidRuntime error log: empty.

Synthetic fixture used an html/head/style/body document with paragraphs and an HTTPS anchor. The title-only save preserved the original string exactly. Explicit source replacement with `<p>Edited-source</p>` rendered after restart.

This is not the source fork's 22-test suite. Real Outlook round-trip, full theme/accessibility/rotation coverage and mobile Morphe Manager execution remain open. Scope: event editor only.
