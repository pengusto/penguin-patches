# Finamp album and track information: preliminary installed-APK captures

These are preliminary, unedited captures from one disposable Android 15 / API 35 ARM64 emulator, using the same synthetic Jellyfin account, library, display and Light theme. `album-before`/`album-after` and `track-before`/`track-after` are matched original/experimental-candidate surfaces. The dialog and offline captures show the text. No real music, account or physical device was used. XML files are the corresponding accessibility hierarchies.

- Original official [Finamp 1.0.1 universal APK](https://github.com/finamp-app/finamp/releases/download/1.0.1/Finamp-1.0.1-universal.apk): SHA-256 `0fc61e5cb80d37b10a735851c2217cbc855b3f1a04bde577187658531ffef581`.
- Before: the original APK re-signed with the disposable test key, SHA-256 `a43d9464b2bc778b506575ab7a3a84c4368a94f6f51a9dff7902d2b7828a67a8`.
- After: an **experimental manually assembled candidate**, SHA-256 `5feca200ddd5b88f8d3afd12c694edb06880cc9616ad77120b3c3ca64719a1a6`, using the same disposable test key. It is **not a Morphe output**. Comparison against the official APK found that the sole changed retained entry is ARM64 `libapp.so`; non-ARM64 native libraries were removed. The snapshot SHA-256 is `7381bc5766812eb10ff184c55c6cf578cb655d1f1d923e1b9030519fb6a72f72`.
- Candidate [source](https://github.com/pengusto/finamp/commit/683326441fca584ad190c6356f6fbe81faa19229): Finamp 1.0.1 plus the proposed upstream feature. Compiled using Flutter 3.47.2, framework `d3b14c876900e553bc736ca19295fc09e3853e8e`, engine `a804b261645ef8c13eb3d5c44a5c2fb0340c5539` and Dart 3.13.2; original snapshot header and size matched.
- Offline: the synthetic album and all eight tracks were downloaded normally. After switching Finamp offline and removing the fixture's ADB reverse connection, both texts remained visible (`offline-album`, `offline-track`). The fixture sends `Overview` in list results only when requested, preventing a false-positive sync test. This is not a live-Jellyfin test.
- This evidence does not establish a completed Morphe bundle, Manager acceptance or compatibility with other APK versions. Those require separate verification.

Capture SHA-256 values (PNG and matching raw XML):

```text
e33f003d002f9625a332396f53dd7f8854bee98b843f683bf4b557416dadbda0  album-after.png
db7f06703f3b80ef186fc3a758301cfccb615ac53bce25d11b2b84d0354e8487  album-before.png
faae01e1b4dba76b74a3000b5fabd49f6deb16239fec7e7b532b7759eb983865  album-dialog.png
3afd2b207c02f78260f6a93e7f0e3c08fac4f966331e57f09f1b2388ce2874ca  offline-album.png
508dd6a0a90f5c5de1af680b71b72f05a423d0c5379d8ff44a78efc772da8965  offline-track.png
b257c2b4672df1c092ade6a8a6decace33e9be02678596c0e13d1287d34c42d3  track-after.png
805c6a07df2c8c5ea1d2ef786d780a03df97814afc5b87d1ec1607051282bd0b  track-before.png
b23268971ad7766d35f76dd43926939de46d07f420a7be6720cb420c8d2c22ca  track-dialog.png
0c337611f0547b49ac5e2895c9590b4f98107f8e98ba72420d1057ad93516c6f  album-after.xml
eaaa25bbf9df30f0cb8d88b6da008903de419228b66091119b49fb1cb3d7d53a  album-before.xml
eac72ed945af6d21eeadf4e8b95531b25434a60345de509e381f80831bf00f77  album-dialog.xml
eac72ed945af6d21eeadf4e8b95531b25434a60345de509e381f80831bf00f77  offline-album.xml
64ade676b39f58893a6c0407cc463b3c5731cf12ae06ffde05b103682f1f7e9c  offline-track.xml
e283c84be66568ce4639bcc3a7d570415fff1fdb51ac25e29de8b75bbbe4604a  track-after.xml
372757c21afc3ed895545e53d47c920fda4ccbf550c8f1d212517836c54c2ba6  track-before.xml
64ade676b39f58893a6c0407cc463b3c5731cf12ae06ffde05b103682f1f7e9c  track-dialog.xml

```
