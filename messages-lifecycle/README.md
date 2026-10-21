# Messages #927 / #928

Official Fossify Messages 1.10.1, isolated Android 15 ARM64 emulator, synthetic emulator-injected SMS only.

#927: open thread, Home (not Back), incoming SMS, inspect provider while activity stopped, open MainActivity with clear-top. Original read=1/no unread count; patched read=0/unread count 1.

#928: keep thread visible, incoming SMS, Back then notification shade. Original read=1 with remaining notification; patched read=1 without a Messages notification. No manual notification dismissal between receipt and screenshot.

18 lifecycle/provider/fallback assertions plus 6 existing notification assertions pass. Each new patch also applies separately. Original-to-patched same-test-certificate update retained synthetic conversations.

Limits: real MMS, physical OEM launchers/multiwindow and on-device Morphe are unverified. Original diagnosis/fork credited to fatehiman: https://github.com/fatehiman/peppa-ai-messages/commit/44d2d3876088522785bb2f5d43b54226a6dce555 .
