import importlib.util
from pathlib import Path
import tempfile
import unittest
import zipfile

HERE = Path(__file__).parent


def load(name):
    spec = importlib.util.spec_from_file_location(name, HERE / (name + ".py"))
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


split = load("split_bundles")
verify = load("verify_split_bundles")


class SplitBundlesTest(unittest.TestCase):
    def test_split_preserves_payloads_and_excludes_other_apps(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            original = root / "full.mpp"
            with zipfile.ZipFile(original, "w") as z:
                z.writestr("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\r\nName: Penguin Patches\r\n\r\n")
                z.writestr("io/github/pengusto/patches/finamp/Finamp.class", b"finamp")
                z.writestr("io/github/pengusto/patches/calendar/Calendar.class", b"calendar")
                z.writestr("finamp/1.0.1/1.pfd", b"payload")
                z.writestr("extensions/calendar.mpe", b"extension")
                z.writestr("classes.dex", b"dex\nold")
            d8 = root / "d8"
            d8.write_text("#!/bin/sh\nwhile [ \"$1\" != --output ]; do shift; done\nprintf 'dex\\nnew' > \"$2/classes.dex\"\n")
            d8.chmod(0o755)
            for kind in ("standard", "finamp"):
                output = root / (kind + ".mpp")
                split.make_bundle(original, output, kind, d8, root / "android.jar", root / "classpath.jar")
                verify.verify(original, output, kind)
                with zipfile.ZipFile(output) as z:
                    names = set(z.namelist())
                    self.assertEqual("finamp/1.0.1/1.pfd" in names, kind == "finamp")
                    self.assertEqual("extensions/calendar.mpe" in names, kind == "standard")

    def test_verifier_rejects_a_changed_payload(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp)
            full = root / "full.mpp"
            bad = root / "standard.mpp"
            with zipfile.ZipFile(full, "w") as z:
                z.writestr("META-INF/MANIFEST.MF", "Name: Penguin Patches\r\n")
                z.writestr("io/github/pengusto/patches/finamp/A.class", b"a")
                z.writestr("finamp/1.0.1/1.pfd", b"native")
                z.writestr("extensions/calendar.mpe", b"correct")
                z.writestr("classes.dex", b"dex\nfull")
            with zipfile.ZipFile(bad, "w") as z:
                z.writestr("META-INF/MANIFEST.MF", "Name: Penguin Patches - Standard\r\n")
                z.writestr("extensions/calendar.mpe", b"wrong")
                z.writestr("classes.dex", b"dex\nnew")
            with self.assertRaisesRegex(ValueError, "changed entry"):
                verify.verify(full, bad, "standard")


if __name__ == "__main__":
    unittest.main()
