# Pengusto Patches

Personal Android app fixes compatible with Morphe Manager. Independent project; not affiliated with Morphe or Fossify.

**Status: project setup. No patch bundle has been released yet.** The first planned patch makes Fossify Calendar's Outlook/Exchange descriptions readable while preserving their original source.

## Planned patches

| App | Patch | Status |
| --- | --- | --- |
| Fossify Calendar 1.12.0 | Readable Outlook/Exchange descriptions | Development planned; existing source fix is available in the Calendar fork |

[Calendar implementation and screenshots](https://github.com/pengusto/Calendar/pull/1) · [Existing installable fork](https://github.com/pengusto/Calendar/releases)

## Using future releases

Once a tested `.mpp` release exists, add this repository as a custom source in Morphe Manager, select a supported original APK and apply the desired patches. This project currently has nothing installable through Morphe.

Morphe signs patched apps on the device. Back up its signing key to keep updates installable. Patched APKs with a different certificate cannot update official apps in place; each patch must document package identity, installation and migration before release.

## Development

Work on `dev`; keep `main` for reviewed releases. Run `./gradlew :patches:buildAndroid` with Java 21 and Android SDK. The official Morphe GitHub Packages registry requires an authenticated account with `read:packages`. Use environment variables `GITHUB_ACTOR` and `GITHUB_TOKEN`; never commit credentials. See [official setup](https://github.com/MorpheApp/morphe-patcher/blob/main/docs/2_1_setup.md).

The official template's semantic release workflow is retained but **manual only** during development. Never publish an empty bundle or untested patch. Releases generate patch metadata and changelogs; do not edit generated metadata manually.

## License and provenance

GPL-3.0. Based on [Morphe's official patches template](https://github.com/MorpheApp/morphe-patches-template), initial template commit `d4fa104`. LICENSE and NOTICE are retained. Runtime code derived from Fossify must preserve its attribution.
