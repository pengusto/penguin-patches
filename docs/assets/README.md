# README assets

| Asset | Use / origin |
| --- | --- |
| `readme-hero-dark.png` | README hero, built-in OpenAI Imagegen, 2026-10-28. Selected concept 2 plus the existing dark tailor banner as identity references. |
| `readme-hero-light.png` | Light-theme edit of the dark hero, same tool/date. |
| `add-to-morphe.svg` | Local text link button; no third-party logo, script, font download or remote asset. |
| `add-to-morphe-qr.png` | Source URL QR retained from the existing setup. |
| `penguin-patches-banner-tailor.png`, `penguin-patches-banner-tailor-dark.png` | Original light/dark brand illustrations, retained unchanged. |
| `penguin-patches-banner-purple.png` | Existing maintainer identity reference, retained unchanged. |

The full README stays native Markdown/HTML. App screenshots remain separate,
unedited evidence files. See [provenance and license scope](../PROVENANCE.md).

## Production prompts

Built-in Imagegen was used, without CLI/API fallback. These prompts produced the
two project assets; neither the generated full-page concept nor its app icons
or simulated app UI is used as evidence.

### Dark hero

```text
Use case: compositing. Asset type: single wide GitHub README hero banner, 1800x650 landscape composition, dark theme. Image 1 is the approved overall README design reference, use ONLY its top hero section as layout inspiration. Image 2 is the existing Penguin Patches brand character identity reference. Create ONLY the hero banner, not a webpage or screenshot. Deep near-black #0d1117 backdrop. Left 60 percent: small warm cream project wordmark 'Penguin Patches' in upper left, then huge very bold clean white rounded sans headline on two lines exactly 'Your apps.' and 'A little better.'. Right 40 percent: the SAME soft black and cream textile penguin with orange beak, detailed fabric stitches and purple rim light, happily sewing the mint and orange fabric patch. It should feel like a polished close-up of the approved reference hero. Keep all typography comfortably inside edges, highly legible at 800px width. Do not include subtitle, buttons, patch counts, dates, app logos, app screenshots or any UI. Only the exact project name and headline. No mockup border or watermark. Keep project name visually secondary to headline.
```

### Light hero

```text
Use case: lighting-weather. Edit target: this exact approved Penguin Patches hero banner. Create its LIGHT THEME companion. Change ONLY near-black background to warm ivory #fffaf0, headline white to deep teal #123e43, and project-name cream to deep teal. Keep the EXACT same composition, output aspect ratio, penguin face, size, position, fur/fabric textures, sewing needle, purple thread, mint/orange fabric, decorative stitches, typography shapes and text. The text must remain exactly 'Penguin Patches', 'Your apps.', 'A little better.' in the same places at same sizes. Soften the purple rim lighting to suit daylight but keep character identity unchanged. No buttons, additional text, counts, dates, logos, screenshots or UI.
```
