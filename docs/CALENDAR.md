# First patch: Fossify Calendar

Target for initial development: official Fossify Calendar 1.12.0 (`org.fossify.calendar`), APK `calendar-23-foss-release.apk`.

Official release: https://github.com/FossifyOrg/Calendar/releases/tag/1.12.0

Published original APK SHA-256: `d43b2df05d5702bea760a7ab03402977ba72e446e5932038efbdab248a5de987`.

Reference source fix: https://github.com/pengusto/Calendar/pull/1, commit `d4f1825`.

Acceptance criteria:
- Exchange HTML descriptions display readable native paragraphs/links; plain text stays unchanged.
- Original source remains exact on title-only save, including whitespace.
- Explicit source editing works; no scripts/WebView/remote image loads.
- Themes, accessibility font scale and rotation remain usable.
- Patched APK installs and updates with consistent package/signing identity; installation and local-calendar migration are documented.
- Test patch application against the official APK, install/UI/save on Android, then real Outlook round-trip on a test event. Do not claim that round-trip before evidence.

Current blocker: local GitHub login lacks read:packages for the official Morphe build plugin. No working Calendar Morphe patch exists yet.
