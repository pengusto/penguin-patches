# Contacts #327 and Music Player #50

Published Penguin Patches v1.0.0-dev.5 adds two independent patches: Contacts 1.6.0 merged email/note detail display and Music Player 1.8.4 per-file MP3/Opus artwork. Official APKs reproduced before changes; screenshots compare original versus patched official APK at 1080x1920 on isolated Android 15 ARM64, synthetic data only.

Contacts: 13 native patch assertions; one portable native source Android test. Emails/notes are presentation copies; no provider/model mutation, labels and source editing retained. Organization/share-export are outside scope.
Music: MP3/Opus color assertions, missing/empty/no-cover/corrupt and malformed metadata fallback; two native source Android tests include original callback output and file-hash preservation. Bounded read-only OpusTags parser needed because platform reader and existing tag library do not expose Opus cover here. No new dependency.

Both source builds/Lint/Detekt passed. Source candidates published in pengusto/Contacts branch fix/327-merged-contact-fields (935891f) and pengusto/Music-Player branch fix/50-track-specific-artwork (ba33608). No upstream PR submitted pending maintainer green light.

Physical devices, real contact account synchronization, extra audio formats, document-only music URIs and existing recycled-view callback races remain unverified. Patch compatibility is exact-version only. Signed updates used one disposable test key; original synthetic provider rows and audio hashes retained. Full local build logs remain under work/contact-music; public evidence contains only synthetic screenshots and short test results.
