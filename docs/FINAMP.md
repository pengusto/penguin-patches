# Finamp source contributions

Checked 2026-10-28. [Finamp](https://github.com/finamp-app/finamp) is an MPL-2.0
Jellyfin music client built with Flutter/Dart. Its
[contribution guide](https://github.com/finamp-app/finamp/blob/redesign/CONTRIBUTING.md)
requests `redesign` as the source/PR branch.

## Custom-section title validation

- Issue: [#1798](https://github.com/finamp-app/finamp/issues/1798).
- Upstream PR: [#1830 — Fix custom home-section title validation while typing](https://github.com/finamp-app/finamp/pull/1830).
- Inspected/tested base: `b72ba46a45eb45836516f23e3df989c89c693c88` on `redesign`.
- Fix commit: [`7d30912`](https://github.com/pengusto/finamp/commit/7d30912).

When creating a custom home-screen section, entering a title changes a string
but never rebuilds its validation. Save stays disabled and the missing-title
message/preview remain stale. Clearing a previously valid title has the inverse
problem: Save still appears enabled.

The source fix rebuilds the editor after title changes and retains one
`TextEditingController` for the editor's lifetime. Recreating that controller on
each rebuild would reset selection and IME composition. The retained controller
is disposed with the editor.

### Verification

Flutter **3.47.8**, Dart **3.13.7**, macOS ARM64:

```sh
flutter pub get
flutter test --no-pub
dart analyze lib/components/HomeScreen/home_section_editor.dart test/home_section_editor_test.dart
dart format --output=none --set-exit-if-changed lib/components/HomeScreen/home_section_editor.dart test/home_section_editor_test.dart
git diff --check
```

All **29 tests pass**, including four new full-editor widget tests:

1. Entering a title updates Save, required-title validation and the preview.
2. Clearing an existing title disables Save and restores the validation message.
3. Editing in the middle preserves selection and the IME composing range.
4. Save returns the entered title with the original section type, library and
   sorting configuration.

Three of these four cases fail on the unchanged base. The preview's network
loading is isolated through provider overrides; tests use no Jellyfin account
or personal database. Targeted Dart analysis reports no issues; formatting and
whitespace checks pass. No generated schema, API, settings or localization files
change.

This is **source-level/widget verification**. No Android APK build, real device,
Android keyboard or live Jellyfin acceptance was performed. Upstream CI/review
and merge remain separate from the passing local tests; check the live PR.

## Morphe feasibility

Flutter release apps compile Dart to native machine code; see the
[Flutter architectural overview](https://docs.flutter.dev/resources/architectural-overview).
The home-section editor and the queued Finamp logic fixes are Dart code, whereas
Penguin's existing app hooks operate on Android DEX/resources. Those hooks do not
directly replace this Dart widget implementation.

Accordingly, Finamp currently uses upstream source contributions. It has **no
declared Morphe patch, supported original APK, installable Penguin APK or new
catalogue entry**. A native-library patch, Flutter-specific engine or a verified
rebuild/distribution path would require its own investigation. Source tests and
an upstream PR cannot establish binary APK compatibility.

The [contribution queue](ROADMAP.md) records the next Finamp clusters and competing
upstream work. Preserve Finamp's MPL-2.0 terms for source derivatives; Penguin's
GPL license does not replace the upstream license.
