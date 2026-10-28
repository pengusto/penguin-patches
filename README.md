# Penguin Patches

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="docs/assets/penguin-patches-banner-tailor-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="docs/assets/penguin-patches-banner-tailor.png">
  <img src="docs/assets/penguin-patches-banner-tailor.png" alt="Penguin Patches — a penguin stitching a repair patch onto a fabric app tile in an embroidery hoop" width="1800">
</picture>

**Your apps. A little better.**

Focused Android fixes, ready for Morphe.

<a href="https://morphe.software/add-source?github=pengusto/penguin-patches"><img src="docs/assets/add-to-morphe.svg" alt="Add to Morphe →" width="242" height="52"></a>

<!-- PATCHES_SUMMARY_START -->
**16 patches · 11 apps · [v1.1.0](https://github.com/pengusto/penguin-patches/releases/tag/v1.1.0)**
<!-- PATCHES_SUMMARY_END -->

[Before & after](docs/PATCH_EXAMPLES.md) · [Supported apps](#generated-patch-catalogue) · [Setup & signing](#patch-an-app) · [Latest release](https://github.com/pengusto/penguin-patches/releases/latest)

**Before installing:** use the exact original APK in your app's guide, verify your backup and keep your signing key. [Read the setup guide →](docs/SETUP.md)

## Finamp, with the album artist first.

Album artists first. Other credits alphabetical. More modes in settings.

| Before · original code | After · Song artist order |
| :---: | :---: |
| <img src="tests/finamp/evidence/2026-10-28/credits-before.png" alt="Finamp original: track credits read A Guest, B Guest, Z Lead" width="360"> | <img src="tests/finamp/evidence/2026-10-28/credits-after.png" alt="Finamp patched: album artist Z Lead appears first, followed by A Guest, B Guest" width="360"> |

Real Android APK captures with synthetic music data. [Evidence & limits](tests/finamp/evidence/2026-10-28/README.md).

**[Explore the 3 Finamp patches →](docs/FINAMP_NATIVE.md)**

Choose song artist order, fix custom-section title validation or load artist pages faster. For the official **Finamp 1.0.1 universal APK → ARM64 output**.

[See the title fix](docs/PATCH_EXAMPLES.md#fix-section-title-validation) · [See artist-page loading](docs/PATCH_EXAMPLES.md#load-artist-pages-faster)

| Feeder | Infinity+ |
| :--- | :--- |
| **Get closer to article images.** Tap to open a separate viewer; pinch, pan and double-tap to inspect the details. | **Use your own Reddit OAuth client.** Open the existing sign-in flow without the app's subscription gate. Reddit API approval/access is still required. |
| [Explore the Feeder patch →](docs/FEEDER.md) · [Before / after](docs/PATCH_EXAMPLES.md#zoom-article-images) | [Set up Infinity+ →](docs/INFINITY.md) · [Before / after](docs/PATCH_EXAMPLES.md#configure-custom-oauth-for-infinity) |

<a id="the-fixes"></a>
<a id="generated-patch-catalogue"></a>

<!-- PATCHES_START -->
<details>
<summary><strong>All 11 apps and supported versions</strong></summary>

> **[v1.1.0](https://github.com/pengusto/penguin-patches/releases/tag/v1.1.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;16 patches total
<details>
<summary>📦 Fossify Messages&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**🎯 Supported versions:**

| 1.10.1 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="avoid-notifications-for-visible-conversations"></a>[Avoid notifications for visible conversations](docs/MESSAGES.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#avoid-notifications-for-visible-conversations) | Check visible conversation on the main thread before posting an incoming-message notification. |  |
| <a id="dismiss-notifications-when-marking-conversations-read"></a>[Dismiss notifications when marking conversations read](docs/MESSAGES.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#dismiss-notifications-when-marking-conversations-read) | Remove the selected conversation's notification after Mark as read in the conversation list. |  |
| <a id="keep-background-conversations-unread"></a>[Keep background conversations unread](docs/MESSAGES.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#keep-background-conversations-unread) | Do not mark incoming messages read when their conversation is left in the background. |  |

</details>

<details>
<summary>📦 Fossify Clock&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.6.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="choose-upcoming-alarm-notification-time"></a>[Choose upcoming alarm notification time](docs/CLOCK.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#choose-upcoming-alarm-notification-time) | Choose the advance notice in Morphe, with separate notifications and skip actions for each alarm occurrence. Keeps native ringing times and alarm definitions. | • Upcoming alarm notice (minutes) |

</details>

<details>
<summary>📦 Infinity+&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 8.3.4 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="configure-custom-oauth-for-infinity"></a>[Configure custom OAuth for Infinity+](docs/INFINITY.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#configure-custom-oauth-for-infinity) | Use a configured Reddit OAuth client consistently through login and token exchange in Infinity+ 8.3.4, without the app subscription gate. | • OAuth client ID<br>• Registered redirect URI<br>• API user agent |

</details>

<details>
<summary>📦 Finamp&nbsp;&nbsp;•&nbsp;&nbsp;3 patches</summary>
<br>

**🎯 Supported versions:**

| 1.0.1 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="fix-section-title-validation"></a>[Fix section title validation](docs/FINAMP_NATIVE.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#fix-section-title-validation) | Update validation and enable Save when a custom home-section title is entered (#1830). Requires the original official Finamp 1.0.1 APK. Produces ARM64-only output; other native architectures are removed. |  |
| <a id="load-artist-pages-faster"></a>[Load artist pages faster](docs/FINAMP_NATIVE.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#load-artist-pages-faster) | Load artist previews and track counts without downloading the full discography (#1832). Requires the original official Finamp 1.0.1 APK. Produces ARM64-only output; other native architectures are removed. |  |
| <a id="song-artist-order"></a>[Song artist order](docs/FINAMP_NATIVE.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#song-artist-order) | Choose song-credit display order, including album artists first, metadata order and alphabetical (#1831). Requires the original official Finamp 1.0.1 APK. Produces ARM64-only output; other native architectures are removed. |  |

</details>

<details>
<summary>📦 Fossify File Manager&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.6.2 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="keep-favorites-consistent-after-folder-rename-or-removal"></a>[Keep favorites consistent after folder rename or removal](docs/FILE_MANAGER.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#keep-favorites-consistent-after-folder-rename-or-removal) | Update nested favorite paths after successful single or multiple renames and remove nested favorites with their deleted parent. |  |

</details>

<details>
<summary>📦 Tasks.org&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 15.13 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="navigate-to-the-parent-task"></a>[Navigate to the parent task](docs/TASKS.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#navigate-to-the-parent-task) | Add a parent-task arrow to the existing editor. Keeps Back behavior and the current filter; asks whether to save, discard or cancel when the draft has changes. Official project F-Droid APK 15.13 only. |  |

</details>

<details>
<summary>📦 Fossify Music Player&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.8.4 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="prefer-each-track-s-embedded-cover"></a><a id="prefer-each-tracks-embedded-cover"></a>[Prefer each track's embedded cover](docs/MUSIC.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#prefer-each-track-s-embedded-cover) | Use file-specific cover artwork before shared album art, retaining the original fallback and worker callback. |  |

</details>

<details>
<summary>📦 Fossify Calendar&nbsp;&nbsp;•&nbsp;&nbsp;2 patches</summary>
<br>

**🎯 Supported versions:**

| 1.12.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="readable-calendar-descriptions"></a>[Readable Calendar descriptions](docs/CALENDAR.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#readable-calendar-descriptions) | Readable day/agenda summaries and native Outlook/Exchange HTML preview with explicit source editing; preserves the original editor text. |  |
| <a id="separate-all-day-reminder-defaults"></a>[Separate all-day reminder defaults](docs/CALENDAR_REMINDERS.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#separate-all-day-reminder-defaults) | Choose three separate default reminders for new all-day events in Calendar settings. Keeps timed-event defaults, existing events and the use-last-reminders setting. |  |

</details>

<details>
<summary>📦 Kvaesitso&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.41.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="restore-widget-editing-from-the-search-menu"></a>[Restore widget editing from the search menu](docs/KVAESITSO.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#restore-widget-editing-from-the-search-menu) | Open the existing widget editor from the search bar menu when the Edit button is hidden, without changing the preference or stored widgets. |  |

</details>

<details>
<summary>📦 Fossify Contacts&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 1.6.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="show-emails-and-notes-from-merged-contacts"></a>[Show emails and notes from merged contacts](docs/CONTACTS.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#show-emails-and-notes-from-merged-contacts) | Include duplicate contact emails and notes in the detail view without changing stored contact records. |  |

</details>

<details>
<summary>📦 Feeder&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 2.25.0 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| <a id="zoom-article-images"></a>[Zoom article images](docs/FEEDER.md)<br>[Before / after](docs/PATCH_EXAMPLES.md#zoom-article-images) | Open article images in a separate viewer with pinch, pan, double-tap and accessible zoom controls. Linked images offer View image or Open link. Uses Feeder's existing image loader and network policy. Official project F-Droid APK 2.25.0 only. |  |

</details>

</details>
<!-- PATCHES_END -->

<a id="patch-an-app"></a>

<details>
<summary><strong>Setup, signing & verification</strong></summary>

1. [Add the source to Morphe](https://morphe.software/add-source?github=pengusto/penguin-patches).
2. Pick the exact original APK from the app guide, select patches and set their options.
3. Patch, sign and install with your backed-up Morphe key.

**Check your backup before migration.** These patches keep the original package ID. A different signing certificate cannot update an official installation in place; read the [full setup guide](docs/SETUP.md) before uninstalling anything.

Finamp passes [Android Manager 2.2.0 acceptance on an API 35 ARM64 emulator](tests/finamp/evidence/2026-10-28-manager/README.md). Physical devices remain unverified; other apps retain their guide-specific limits. [Verification record](docs/VERIFICATION.md) · [Report a problem](docs/SETUP.md#report-a-problem).

Stable releases come from `main`; `dev` carries pre-releases. Enable pre-releases in Morphe only if you intend to try upcoming changes.

</details>

<a id="development"></a>
<a id="upstream-contributions-and-next-fixes"></a>
<a id="evidence-feedback-and-limits"></a>
<a id="support-development"></a>
<a id="license-and-provenance"></a>

[Build & contribute](docs/DEVELOPMENT.md) · [Upstream work & roadmap](docs/ROADMAP.md) · [Support development](https://github.com/sponsors/pengusto) · [License & provenance](docs/PROVENANCE.md)

Independent project; not affiliated with Morphe or the original app projects. Code: [GPL-3.0](LICENSE), with project-name terms and upstream credits in [NOTICE](NOTICE). [Illustration & screenshot credits](docs/PROVENANCE.md).
