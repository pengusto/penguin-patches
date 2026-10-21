# Penguin Patches

Personal Android app fixes compatible with Morphe Manager. Independent project; not affiliated with Morphe or Fossify.

**Status: tested patches for Calendar, Messages and File Manager; distributed as pre-releases.** Each patch declares only its tested original APK version.

## Patches

| App | Patch | Status |
| --- | --- | --- |
| Fossify Calendar 1.12.0 | Readable Outlook/Exchange descriptions | Pre-release; Android 15 emulator tested |
| Fossify Messages 1.10.1 | Dismiss notifications after Mark as Read | Android 15 emulator tested; [details](docs/MESSAGES.md) |
| Fossify File Manager 1.6.2 | Preserve nested favorites after folder renames/removal | Android 15 emulator tested; [details](docs/FILE_MANAGER.md) |

[Calendar implementation and screenshots](https://github.com/pengusto/Calendar/pull/1) · [Existing installable fork](https://github.com/pengusto/Calendar/releases)

## Installation

Scan this QR code with your Android phone, or tap it, to [add Penguin Patches to Morphe](https://morphe.software/add-source?github=pengusto/penguin-patches):

<a href="https://morphe.software/add-source?github=pengusto/penguin-patches"><img src="docs/assets/add-to-morphe-qr.png" alt="QR code: add Penguin Patches to Morphe" width="240" height="240"></a>

Then enable pre-releases for this source in Morphe Manager and choose the original official APK version listed above. For Calendar, enable **Readable Calendar descriptions**. See [installation and test details](docs/CALENDAR.md). The patch covers the event editor and day/agenda list descriptions. Widget and notification summaries remain outside its scope.

Morphe signs patched apps on the device. Back up its signing key to keep updates installable. Patched APKs with a different certificate cannot update official apps in place; each patch must document package identity, installation and migration before release.

## Development

Work on `dev`; keep `main` for reviewed releases. Run `./gradlew :patches:buildAndroid` with Java 21 and Android SDK. The official Morphe GitHub Packages registry requires an authenticated account with `read:packages`. Use environment variables `GITHUB_ACTOR` and `GITHUB_TOKEN`; never commit credentials. See [official setup](https://github.com/MorpheApp/morphe-patcher/blob/main/docs/2_1_setup.md).

The official template's semantic release workflow is retained but **manual only** during development. Never publish an empty bundle or untested patch. Releases generate patch metadata and changelogs; do not edit generated metadata manually.

## License and provenance

GPL-3.0. Based on [Morphe's official patches template](https://github.com/MorpheApp/morphe-patches-template), initial template commit `d4fa104`. LICENSE and NOTICE are retained. Runtime code derived from Fossify must preserve its attribution.

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0-dev.3](https://github.com/pengusto/penguin-patches/releases/tag/v1.0.0-dev.3)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;3 patches total
<details open>
<summary>📦 Fossify Messages&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.10.1 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Dismiss notifications when marking conversations read](#dismiss-notifications-when-marking-conversations-read) | Remove the selected conversation's notification after Mark as read in the conversation list. |  |

</details>

<details open>
<summary>📦 Fossify File Manager&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.6.2 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Keep favorites consistent after folder rename or removal](#keep-favorites-consistent-after-folder-rename-or-removal) | Update nested favorite paths after successful single or multiple renames and remove nested favorites with their deleted parent. |  |

</details>

<details open>
<summary>📦 Fossify Calendar&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.12.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Readable Calendar descriptions](#readable-calendar-descriptions) | Readable day/agenda summaries and native Outlook/Exchange HTML preview with explicit source editing; preserves the original editor text. |  |

</details>

<!-- PATCHES_END -->
