# Calendar HTML surfaces: Manager acceptance

Captured 2026-11-04 on a disposable Android 15 ARM64 emulator (1080 × 1920),
using synthetic offline event data. No real calendar or account is included.

- **Before:** official Calendar 1.12.1 APK, re-signed with the disposable test
  certificate. The original APK SHA-256 is
  `dae815ce83285facf9fd8648b8c4f039700d95126325db124521eba479b270ba`.
- **After:** the same official APK patched and exported by Morphe Manager 2.2.0
  from local bundle SHA-256
  `784835c2aa2482183f23b7434edeaf99bf9f415a1789e0cd8771471696c24e8c`.
  The Manager export SHA-256 is
  `dd6f741188a0a741a10b144d74e993cf234d913fa97e0a2ee727432044aca90c`.
- **Widget:** the app's actual RemoteViews are rendered in a dedicated test
  surface, not an installed launcher widget. Time, description, text color,
  device and theme are matched. Raw PNG and accessibility XML are included.
- **Notification:** the app's posted notification is shown in Android's actual
  notification shade with the same synthetic title, description and event time.
  The additional serial-console notice belongs to the emulator.
- Manager applied the final local bundle to Calendar 1.12.0 with both Calendar
  patches selected. The export SHA-256 was
  `8efc79760fe5972520cb9a6e4fbbe09eeef7b12d59618db48e17bd881845539f`;
  34 HTML-surface and 131 all-day-reminder integration checks passed on that
  actual exported and installed APK. The 1.12.1 Manager export passed 38 checks
  including screenshot visibility; baseline passed 33.
- The original-to-Manager 1.12.1 same-key update retained all app database
  tables (including WAL) and shared preferences in the disposable fixture.

The test signing certificate is used only by the emulator. It is not a user
installation key. The local bundle used an explicitly documented Desktop API
build substitution. The actual release bundle is independently verified by CI;
this evidence does not claim a physical device or live Outlook/CalDAV sync test.
