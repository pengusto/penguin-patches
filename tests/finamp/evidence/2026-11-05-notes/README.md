# Finamp album and track information: installed-APK candidate

Unedited captures from one disposable Android 15 / API 35 ARM64 emulator use the same synthetic Jellyfin account, downloaded album and eight tracks, Light theme and display. `album-before`/`album-after` and `track-before`/`track-after` are the matching original/candidate pairs. The other captures show the dialogs online and offline. The track menu's information entry follows the primary actions and uses the same information icon as the album action. XML files are the corresponding raw accessibility hierarchies.

- Original official [Finamp 1.0.1 universal APK](https://github.com/finamp-app/finamp/releases/download/1.0.1/Finamp-1.0.1-universal.apk): SHA-256 `0fc61e5cb80d37b10a735851c2217cbc855b3f1a04bde577187658531ffef581`.
- Before: original APK re-signed with the disposable test key; SHA-256 `a43d9464b2bc778b506575ab7a3a84c4368a94f6f51a9dff7902d2b7828a67a8`.
- After: official APK patched with **Morphe Desktop 1.18.1** and a **partial local development bundle built using dependency substitution**, then signed with the same disposable key; SHA-256 `24f5ad348f35f24fb23985b15cd1ef0c978efb9bb7711b0d5e2e0ca936166557`. This is not an official CI or released bundle. APK comparison found the sole changed retained entry was ARM64 `libapp.so`; other native ABIs were removed. Snapshot SHA-256 `5fadbc9fa7712e6798bbff08a9b408595203eac1a6299ad1354627e1ef37a962`.
- Backport [source](https://github.com/pengusto/finamp/commit/f57c57bc8f55b22c7926bab81660f23710526075), corresponding to the corrected upstream feature in [PR #1853](https://github.com/finamp-app/finamp/pull/1853). Compiled using Flutter 3.47.2, framework `d3b14c876900e553bc736ca19295fc09e3853e8e`, engine `a804b261645ef8c13eb3d5c44a5c2fb0340c5539` and Dart 3.13.2; the original snapshot header and size matched. The original APK's subsetted Tabler font includes the information glyph.
- Offline: after normal download, both texts remained visible with Finamp offline and the fixture's ADB reverse connection removed. The fixture sends `Overview` in list results only when requested, preventing a false-positive sync test. No live Jellyfin server was used.
- Same-key removal and reapplication of an earlier development build preserved the synthetic login and downloaded-file hashes. The corrected Morphe APK updated that state without clearing it, and its offline test found all eight tracks.
- The full selection matrix, the final bundle and Manager UI have not yet been verified by these captures. No compatibility with another APK version or architecture is implied.

SHA-256 values (PNG and matching raw XML):

```text
b5e1fcff83fb89147dba9897430e7f27743f3e28af2f22689e5738a1fb281c5c  album-after.png
f6d0d9029b0ecb7008d8e9d20b15538555bdf49e1405a9cf732574d3d3ef13a3  album-before.png
13e34074657f5587f4dfc4c42e33c7ed9aacf5afa9420fb52ffe1dff39436cf1  album-dialog.png
611cbdc0b13f3d0cc161bceff864adfcf03416330dfaa0871e922c18712ac02d  offline-album.png
5b9d70607a8a62016dfd836588ba096c342120ae31936c82031eb3d4760464e9  offline-track.png
80cfde5fac1ee1320f008b9694b38facaf9034c9dd27f3bb85a974091b947466  track-after.png
4fb0a9f17fe0c3734c58cdf171979552feab42f8a622c7ba8f6ea446fc6b9cbc  track-before.png
5c16d250f86045a384187af59c19691a53468a184991d2fa3a374e9318e0fe61  track-dialog.png
1d3e4cad1f3b7816fef76d38c9e8e908fc775fdfd7ce2c4ab810098b519439bb  album-after.xml
5b2ae206b5624c0de6c88fc30fdb6be60c6655d9731b147be16131dc465c38b4  album-before.xml
eac72ed945af6d21eeadf4e8b95531b25434a60345de509e381f80831bf00f77  album-dialog.xml
eac72ed945af6d21eeadf4e8b95531b25434a60345de509e381f80831bf00f77  offline-album.xml
64ade676b39f58893a6c0407cc463b3c5731cf12ae06ffde05b103682f1f7e9c  offline-track.xml
e94424b8dea8619c7c19292ec70f12bf7b5c82c1c9d7e2921b2afb2fcb599dd7  track-after.xml
18a5fef5f141af50e7bbc7c694c9b7729906ba13f06f200ab54326fe3ec7fea7  track-before.xml
64ade676b39f58893a6c0407cc463b3c5731cf12ae06ffde05b103682f1f7e9c  track-dialog.xml

```
