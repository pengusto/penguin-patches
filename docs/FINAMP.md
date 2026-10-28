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

## Song artist order

User-selected default: **album artists first, remaining song artists
alphabetically**. [Upstream PR #1831](https://github.com/finamp-app/finamp/pull/1831)
implements the preference on the same `redesign` base; feature commit
[`7c3dd62`](https://github.com/pengusto/finamp/commit/7c3dd62).

Finamp explicitly alphabetizes player chips, track tiles and media labels.
Jellyfin exposes song artists and album artists, without an authoritative
per-song lead/featured role in these fields. Album artists therefore serve as a
hint: promote only those also present in the song credits. If none match, keep
the song's metadata order. Compilation names such as Various Artists are not
inserted as song artists. Matching IDs take priority over display names.

**Layout & Theme → Song artist order** offers:

- Album artists first (new default), other song artists alphabetically.
- Metadata order, for a credit order maintained in the server's metadata.
- First credited artist first, then the remaining artists alphabetically.
- Alphabetical, retaining the old display option.

The preference is used for player/menu chips, track lists and subtitles,
playback history, saved-queue previews and media labels. Changing it refreshes
cached queue labels without reloading audio. Song metadata/tags and playback or
library sorting are not rewritten. Name-only credits remain visible without an
invented artist-navigation target.

Verification of the independent feature branch: **58 tests pass**, including
**33 new tests**. These cover ordering and IDs, unchanged DTOs, offline metadata,
cached queue metadata, actual widget selection/navigation, narrow screens with
enlarged text, persistence of every mode, layout reset and upgrade from an older
Hive settings payload. Android ARM64 **debug APK compilation succeeds** with
Flutter 3.47.8, the existing Android SDK/NDK and the existing Rust toolchain.
The Rust path is required by the app's existing Discord plugin. Analysis has no
errors/warnings on touched files; one existing informational lint remains outside
the modified lines. Generated code, formatting and whitespace checks pass.

Hive field **161** and adapter type **128** store the new preference. The old-to-new
settings path is tested; downgrading to a build without the new adapter is not
claimed as compatible. Real Jellyfin/OEM-device/media-notification acceptance is
not established by these tests. This is a source contribution and debug build,
with no new Morphe catalogue entry. The section-title fix #1830 remains a separate
PR and is not part of the independent artist-order branch.

The separate [combined preview source](https://github.com/pengusto/finamp/tree/12b2f2103186692506523bcc359c27c1ea5103e6)
contains both PRs. Its **62 tests pass** and its debug APK builds and starts to
the welcome screen on the isolated Android 15 ARM64 emulator. It uses package
`com.unicornsonlsd.finamp.debug` and an Android debug certificate, allowing a
separate installation alongside the official app. This is a local preview
artifact, not a new Penguin Morphe release or upstream-signed Finamp release.

## Artist-page loading

[PR #1832](https://github.com/finamp-app/finamp/pull/1832) addresses
[issue #1732](https://github.com/finamp-app/finamp/issues/1732), independently on
the same `redesign` base. Commit:
[`8d6b4b6`](https://github.com/pengusto/finamp/commit/8d6b4b6868481175339137a32d057055e103289e).

The page eagerly loads complete album-artist and performing-artist track lists
for display, and its five-track preview reuses an unbounded list. The fix limits
both preview requests to five records with the selected sort/favorite filter,
uses one `TotalRecordCount` request with `Limit=1` and no optional fields, and
keeps full track enumeration behind playback actions.

Do not add album and performing counts: the sets overlap. Jellyfin's `ArtistIds`
query already includes both track and album credits and counts each matching
item once. Verified against the primary server implementations in
[10.10.7](https://github.com/jellyfin/jellyfin/blob/v10.10.7/Emby.Server.Implementations/Data/SqliteItemRepository.cs#L3271-L3297)
and the [current server source](https://github.com/jellyfin/jellyfin/blob/master/Jellyfin.Server.Implementations/Item/BaseItemRepository.TranslateQuery.cs#L562-L569).
An album-only credit is therefore included by this filter despite the UI's
"performing artist" terminology. This source evidence resolves the uncertainty
raised in the issue discussion; it is not a live-server compatibility test.

Download-status input remains the number of albums plus the performing-query
track count. Loaded albums remain available for download-size warnings. Offline
counts reuse existing local providers. A pending/failed count is hidden while
the rest of the page renders; a confirmed zero is displayed as zero. Refresh
invalidates the metadata provider.

Independent branch verification: **41 tests pass**, including **16 new tests**;
targeted Dart analysis has **no issues**. Code generation, formatting and
whitespace checks pass. Android ARM64 debug APK compilation succeeds with the
same runtime/toolchains documented above. No installation or live Jellyfin/device
acceptance was performed for this third contribution.

The regression runs the actual preview provider with only the API boundary
faked: on unchanged upstream it requests 5 + 10,000 records, totaling **2,148,827
synthetic serialized bytes**. The fix requests 5 + 5 records / **2,092 bytes**.
This quantifies that fixture's preview response, not complete-page memory,
real-library latency or every album request. Tests also cover all six selection
types, filter forwarding, deduplication, count/error handling, disabled preview,
full on-demand favorite playback, local offline-provider reuse and header UI.

This PR is **not included in the two-change combined preview described above**.
It is a separate source contribution, not a new Morphe catalogue entry or signed
upstream release. Current GitHub review/CI/merge status is separate from these
local checks.

## Review evidence and optional three-fix preview

The artist-order request is tracked by
[issue #1833](https://github.com/finamp-app/finamp/issues/1833), linked for closure
by PR #1831. Existing issues received focused evidence updates:
[#1798](https://github.com/finamp-app/finamp/issues/1798#issuecomment-6475184371)
and [#1732](https://github.com/finamp-app/finamp/issues/1732#issuecomment-6475197764).
All three PR descriptions include before/after images and fallback testing links.

[Public widget evidence and reproduction](https://github.com/pengusto/finamp/tree/7e168434482e4e1e7b77a0c37337edb28291e2f8/contribution-evidence/2026-10-28)
uses unchanged upstream `b72ba46` and combined source
[`ec10998`](https://github.com/pengusto/finamp/tree/ec109989ace6143377567a1ec71664ec6863dcc3).
The actual Finamp chips, selector, editor and artist page render synthetic data;
they are not device/live-server screenshots. All **seven PNGs reproduce byte for
byte** in two isolated local clones. Capture suites pass three before/four after
tests, separately from the **78 passing combined app tests**.

The [unofficial ARM64 Android debug preview](https://github.com/pengusto/finamp/releases/tag/penguin-preview-2026-10-28-r2)
now includes **all three fixes**, superseding the earlier two-fix preview as the
recommended testing artifact. The earlier preview/source remains historical.
The new APK builds, installs as an update to that fixture preview and cold-starts
to the welcome screen on the disposable Android 15 ARM64 emulator, with no
observed AndroidRuntime fatal exception. UIAutomator's Flutter labels appear in
`content-desc` as well as `text`; the smoke verification reads both.

The release publishes individual source patches, the combined patch, exact
source, MPL-2.0 license, artifact hashes and verification. Its package remains
`com.unicornsonlsd.finamp.debug`, version `1.0.1-penguin.review.20261028`, code 201,
signed with the same Android debug certificate. Targeted analysis has no
errors/warnings; one existing informational lint remains outside the changed
lines. Physical/OEM-device, live Jellyfin, playback, downloads and lock-screen
acceptance remain open. Upstream workflow runs for all three independent heads
report `action_required` and require maintainer approval.

## Morphe feasibility

Flutter release apps compile Dart to native machine code; see the
[Flutter architectural overview](https://docs.flutter.dev/resources/architectural-overview).
The home-section editor and the queued Finamp logic fixes are Dart code, whereas
Penguin's existing app hooks operate on Android DEX/resources. Those hooks do not
directly replace this Dart widget implementation.

Morphe can also read and write arbitrary APK files, so changing native libraries
is technically possible; Flutter does not make every APK change equally hard.
The specific Dart logic fixes still need their own native patching method and
per-version/architecture verification.

Accordingly, Finamp currently uses upstream source contributions. It has **no
declared Morphe patch, supported original APK or new catalogue entry**. A native-library patch, Flutter-specific engine or a verified
rebuild/distribution path would require its own investigation. Source tests and
an upstream PR cannot establish binary APK compatibility.

The [contribution queue](ROADMAP.md) records the next Finamp clusters and competing
upstream work. Preserve Finamp's MPL-2.0 terms for source derivatives; Penguin's
GPL license does not replace the upstream license.
