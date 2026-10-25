# Infinity+ 8.3.4 custom OAuth — 2026-10-25

Native Android 15 / API 35 ARM64 AOSP emulator captures, original Infinity+ 8.3.4 (versionCode 232). No account data.

- `before-breal-1.4.123.png`: the same APK with Breal 1.4.123 Spoof client; with the billing wrapper at state 4, opening Login shows the Subscription dialog.
- `after-penguin-login.png`: the APK patched only with Penguin's "Configure custom OAuth for Infinity+"; Add an account opens Login with the browser and WebView methods, no Subscription dialog.

Live sign-in, account import, authenticated feed and persistence after restart were also verified with the Penguin build; those captures contain account data and are not published.
