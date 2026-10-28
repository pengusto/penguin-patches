"""Release-render regressions; no APK/build dependencies required."""

import json
from pathlib import Path
import re
import subprocess
import sys
import tempfile
import unittest


ROOT = Path(__file__).resolve().parents[2]
SCRIPT = ROOT / ".github/scripts/generate_patches_readme.py"


class ReleaseReadmeTests(unittest.TestCase):
    def render(self, data=None, expanded=False, readme=None):
        with tempfile.TemporaryDirectory() as tmp:
            metadata = Path(tmp) / "patches.json"
            target = Path(tmp) / "README.md"
            metadata.write_text(json.dumps(data or json.loads((ROOT / "patches-list.json").read_text())))
            original_json = metadata.read_bytes()
            text = readme or (ROOT / "README.md").read_text()
            if expanded:
                text = text.replace("<!-- PATCHES_START -->", "<!-- PATCHES_START EXPANDED -->")
            target.write_text(text)
            command = [sys.executable, str(SCRIPT), "pengusto/penguin-patches", "dev", str(metadata), str(target)]
            result = subprocess.run(command, capture_output=True, text=True)
            self.assertEqual(result.returncode, 0, result.stderr)
            first = target.read_text()
            subprocess.run(command, check=True, capture_output=True)
            self.assertEqual(target.read_text(), first, "regeneration must be idempotent")
            self.assertEqual(metadata.read_bytes(), original_json, "rendering must not modify metadata")
            return first

    def test_current_release_stays_compact_and_links_to_guides(self):
        result = self.render()
        self.assertNotIn("<details open>", result)
        self.assertIn("All 11 apps and supported versions", result)
        self.assertIn("16 patches · 11 apps", result)
        for guide in re.findall(r"\]\((docs/[^)#]+)", result):
            self.assertTrue((ROOT / guide).is_file(), guide)
        self.assertIn("[Song artist order](docs/FINAMP_NATIVE.md)", result)
        self.assertIn("[Separate all-day reminder defaults](docs/CALENDAR_REMINDERS.md)", result)
        self.assertIn('id="prefer-each-track-s-embedded-cover"', result)
        self.assertIn('id="prefer-each-tracks-embedded-cover"', result)

    def test_small_next_prerelease_updates_summary_without_expanding(self):
        data = json.loads((ROOT / "patches-list.json").read_text())
        data["version"] = "v1.2.0-dev.1"
        data["patches"] = [p for p in data["patches"] if p["name"] == "Song artist order"]
        result = self.render(data)
        self.assertNotIn("<details open>", result)
        self.assertIn("1 patch · 1 app", result)
        self.assertIn("/releases/tag/v1.2.0-dev.1", result)
        self.assertIn("`dev`", result)
        self.assertNotIn("/releases/tag/v1.1.0", result)

    def test_expansion_requires_explicit_opt_in(self):
        result = self.render(expanded=True)
        catalogue = result.split("<!-- PATCHES_START EXPANDED -->")[1].split("<!-- PATCHES_END -->")[0]
        self.assertEqual(catalogue.count("<details open>"), 12)

    def test_unknown_and_universal_patches_do_not_gain_dead_links(self):
        data = {"version": "2.0.0", "patches": [
            {"name": "New app fix", "description": "Keep \\1 literal", "compatiblePackages": [
                {"packageName": "example.new", "name": "New app", "targets": [{"version": "2.0"}]}]},
            {"name": "Universal fix", "description": "Works across apps"},
        ]}
        result = self.render(data, readme="Intro\n<!-- PATCHES_START -->\n<!-- PATCHES_END -->\nFooter")
        self.assertNotIn("<details open>", result)
        self.assertIn('id="new-app-fix"', result)
        self.assertIn('id="universal-fix"', result)
        self.assertNotIn("](#", result)
        self.assertIn("Keep \\1 literal", result)
        self.assertTrue(result.endswith("Footer"))


if __name__ == "__main__":
    unittest.main()
