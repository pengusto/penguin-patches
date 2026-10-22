# Prefer each track's embedded artwork before album fallback

Read embedded file artwork before shared album URI in the original asynchronous worker. Use platform metadata for MP3 and a bounded read-only OpusTags picture reader when the platform exposes no Opus cover. Keep album grouping, stored metadata, original callback and album-art fallback.

Build/Lint/Detekt pass; two native Android tests cover distinct MP3/Opus cover pixels, callback fallback, malformed tags and unchanged source file hashes. Included test audio/images are locally generated sine tones and solid colors. Other formats and physical devices remain unverified.

Closes #50
