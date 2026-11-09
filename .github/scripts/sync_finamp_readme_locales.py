#!/usr/bin/env python3
"""Update Finamp Complete labels when the seven-fix release metadata is generated."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
OLD_SLUG = "preview-finamp-complete-all-eight-fixes"
NEW_SLUG = "preview-finamp-complete-all-seven-fixes"
LABELS = {
    "de": ("Finamp Complete: alle acht Änderungen", "Finamp Complete: alle sieben Änderungen"),
    "es": ("Finamp Complete: las ocho mejoras", "Finamp Complete: las siete mejoras"),
    "fr": ("Finamp Complete : les huit améliorations", "Finamp Complete : les sept améliorations"),
    "ja": ("Finamp Complete：8 件の修正をまとめて適用", "Finamp Complete：7 件の修正をまとめて適用"),
    "ko": ("Finamp Complete: 8개 수정 사항", "Finamp Complete: 7개 수정 사항"),
    "pt-BR": ("Finamp Complete: as oito melhorias", "Finamp Complete: as sete melhorias"),
    "zh-CN": ("Finamp Complete：全部八项改进", "Finamp Complete：全部七项改进"),
}


def sync(root):
    metadata = json.loads((root / "patches-list.json").read_text(encoding="utf-8"))
    names = {p["name"] for p in metadata["patches"]}
    if "Finamp complete: all seven fixes" not in names:
        raise ValueError("Expected seven-fix Finamp Complete release metadata")
    changes = []
    for locale, (old_label, new_label) in LABELS.items():
        path = root / "docs/readme" / f"README.{locale}.md"
        text = path.read_text(encoding="utf-8")
        old = f"[{old_label}](../../README.md#{OLD_SLUG})"
        new = f"[{new_label}](../../README.md#{NEW_SLUG})"
        if text.count(old) == 1 and text.count(new) == 0:
            changes.append((path, text.replace(old, new)))
        elif text.count(old) == 0 and text.count(new) == 1:
            continue
        else:
            raise ValueError(f"Unexpected Finamp Complete link in {path}")
    for path, text in changes:
        path.write_text(text, encoding="utf-8")
    return len(changes)


if __name__ == "__main__":
    print(f"Updated {sync(ROOT)} localized Finamp links")
