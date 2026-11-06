#!/usr/bin/env python3
"""Produce independent Android/JVM patch bundles from the verified full bundle."""
import argparse
import json
from pathlib import Path
import shutil
import subprocess
import tempfile
import zipfile

FINAMP_CLASSES = "io/github/pengusto/patches/finamp/"
FINAMP_RESOURCES = "finamp/"


def make_bundle(source, destination, kind, d8, android_jar, classpath):
    with tempfile.TemporaryDirectory(prefix="penguin-split-", dir=destination.parent) as tmp:
        classes = Path(tmp) / "classes.jar"
        dex_dir = Path(tmp) / "dex"
        dex_dir.mkdir()
        with zipfile.ZipFile(source) as original:
            names = original.namelist()
            if len(names) != len(set(names)):
                raise ValueError("Full bundle has duplicate ZIP entries")
            class_names = [n for n in names if n.endswith(".class") and
                           (n.startswith(FINAMP_CLASSES) == (kind == "finamp"))]
            if not class_names or not any(n.startswith(FINAMP_CLASSES) for n in names):
                raise ValueError("Missing expected patch classes")
            with zipfile.ZipFile(classes, "w") as selected:
                for name in class_names:
                    selected.writestr(name, original.read(name))
            cmd = [str(d8), "--release", "--min-api", "26", "--lib", str(android_jar),
                   "--classpath", str(classpath), "--output", str(dex_dir), str(classes)]
            subprocess.run(cmd, check=True)
            dex = dex_dir / "classes.dex"
            if not dex.is_file() or len(list(dex_dir.glob("*.dex"))) != 1:
                raise ValueError("Expected exactly one DEX file")
            with zipfile.ZipFile(destination, "w", allowZip64=True) as result:
                for info in original.infolist():
                    name = info.filename
                    if info.is_dir() or name == "classes.dex":
                        continue
                    if name.endswith(".class") and name not in class_names:
                        continue
                    if name.startswith(FINAMP_RESOURCES) != (kind == "finamp"):
                        if name.startswith(FINAMP_RESOURCES) or name.startswith("extensions/"):
                            continue
                    if name.startswith("extensions/") and kind == "finamp":
                        continue
                    if name == "META-INF/MANIFEST.MF":
                        manifest = original.read(name)
                        old = b"Name: Penguin Patches\r\n"
                        new = f"Name: Penguin Patches - {kind.title()}\r\n".encode()
                        if manifest.count(old) != 1:
                            raise ValueError("Unexpected bundle manifest name")
                        result.writestr(info, manifest.replace(old, new))
                    else:
                        with original.open(info) as src, result.open(info, "w") as dst:
                            shutil.copyfileobj(src, dst, 1024 * 1024)
                result.write(dex, "classes.dex", compress_type=zipfile.ZIP_DEFLATED)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--bundle", type=Path, required=True)
    parser.add_argument("--version", required=True)
    parser.add_argument("--metadata", type=Path, required=True)
    parser.add_argument("--output-dir", type=Path, required=True)
    parser.add_argument("--metadata-dir", type=Path, required=True)
    parser.add_argument("--d8", type=Path, required=True)
    parser.add_argument("--android-jar", type=Path, required=True)
    parser.add_argument("--classpath", type=Path, required=True)
    args = parser.parse_args()
    metadata = json.loads(args.metadata.read_text())
    if metadata["version"] != args.version:
        raise ValueError("Full bundle metadata version does not match release")
    expected_suffix = f"/patches-{args.version}.mpp"
    if not metadata["download_url"].endswith(expected_suffix):
        raise ValueError("Unexpected full bundle download URL")
    for kind in ("standard", "finamp"):
        output = args.output_dir / f"patches-{kind}-{args.version}.mpp"
        make_bundle(args.bundle, output, kind, args.d8, args.android_jar, args.classpath)
        split_metadata = dict(metadata)
        split_metadata["download_url"] = metadata["download_url"][:-len(expected_suffix)] + f"/patches-{kind}-{args.version}.mpp"
        split_metadata["description"] = ("Finamp patches only." if kind == "finamp" else
                                         "Infinity+ and all supported apps except Finamp.") + "\n\n" + metadata["description"]
        target = args.metadata_dir / kind / "patches-bundle.json"
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(json.dumps(split_metadata, indent=2, ensure_ascii=False) + "\n")
        print(f"{kind}: {output.stat().st_size:,} bytes -> {output}")


if __name__ == "__main__":
    main()
