#!/usr/bin/env python3
"""Apply/export one selection through observed Manager 2.2.0 UI, never its API.

Requires the original APK already loaded and Penguin selected. --from-result
uses the observed Home -> Finamp -> Patch path, which reuses that original.
SDK/AVD/output guards come from android_acceptance. No installer/data reset.
"""
import argparse
import hashlib
import json
from pathlib import Path
import re
import subprocess
import time

import android_acceptance as base
from patch_matrix import NAMES, check_contents


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--adb', type=Path, required=True)
    parser.add_argument('--mask', type=int, choices=(1, 2, 4, 7), required=True)
    parser.add_argument('--runtime', choices=('jvm', 'art'), required=True)
    parser.add_argument('--from-result', action='store_true')
    parser.add_argument('--recovery', action='store_true', help='Fresh export proving recovery after a rejected input')
    parser.add_argument('--original-apk', type=Path, required=True)
    parser.add_argument('--native-report', type=Path, required=True)
    args = parser.parse_args()
    base.PACKAGE = 'app.morphe.manager'
    base.OUT_ROOT = base.REPO_ROOT / '.scratch/finamp/manager-2.2.0'
    ui = base.Android(out=base.OUT_ROOT / 'ui', adb=args.adb)
    suffix = '-recovery' if args.recovery else ''
    name = f'manager-mask-{args.mask}-{args.runtime}{suffix}.apk'
    destination = base.OUT_ROOT / 'apks' / name
    remote = '/sdcard/Download/finamp-manager-test/' + name
    if destination.exists() or ui.adb('shell', 'ls', '-d', remote, check=False).strip():
        raise ValueError('Refusing a stale output; use a fresh case')

    def wait_for(label):
        deadline = time.monotonic() + 180
        while time.monotonic() < deadline:
            root, _ = ui.dump()
            try:
                ui.find(root, label)
                return root
            except RuntimeError:
                time.sleep(0.3)
        ui.capture('case-timeout')
        raise RuntimeError('Expected Manager stage absent: ' + label)

    def runtime_lines():
        pid = ui.adb('shell', 'pidof', 'app.morphe.manager').strip()
        text = ui.adb('logcat', '-d', '--pid=' + pid, '-v', 'threadtime')
        return [line for line in text.splitlines() if 'Morphe Patcher: Runtime: name=' in line]

    if args.from_result:
        ui.tap('Home', class_name='android.view.View')
        ui.tap('Finamp', class_name='android.widget.TextView')
        ui.tap('Patch', class_name='android.widget.TextView')
    wait_for('Fix section title validation')
    for bit, patch in NAMES.items():
        root, _ = ui.dump()
        states = [n.get('content-desc') for n in root.iter('node')
                  if n.get('content-desc') in (patch + ', Enabled', patch + ', Disabled')]
        if len(states) != 1:
            raise RuntimeError('Missing distinct patch row: ' + patch)
        if (states[0].endswith(', Enabled')) != bool(args.mask & bit):
            ui.tap(patch, class_name='android.widget.TextView')
    root, _ = ui.dump()
    assert any(n.get('text') == '0/5' for n in root.iter('node')), 'Unrelated Morphe patches selected'
    for bit, patch in NAMES.items():
        expected = patch + (', Enabled' if args.mask & bit else ', Disabled')
        assert any(n.get('content-desc') == expected for n in root.iter('node'))
    ui.capture(f'mask-{args.mask}-{args.runtime}{suffix}-selection')
    previous_runtime = runtime_lines()
    ui.tap('Patch', class_name='android.widget.TextView')
    wait_for('Patched')
    ui.capture(f'mask-{args.mask}-{args.runtime}{suffix}-success')
    lines = runtime_lines()
    if not lines or lines == previous_runtime:
        raise RuntimeError('No new actual patcher invocation recorded')
    runtime = re.search(r'Runtime: name=([a-z]+)', lines[-1]).group(1)
    if runtime != args.runtime:
        raise RuntimeError('Actual runtime differs: ' + runtime)
    ui.tap('Save', class_name='android.view.View')
    base.PACKAGE = 'com.android.documentsui'
    ui.type_text(name, index=0, replace=True)
    ui.tap('SAVE')
    base.PACKAGE = 'app.morphe.manager'
    wait_for('Patched')
    ui.capture(f'mask-{args.mask}-{args.runtime}{suffix}-exported')
    destination.parent.mkdir(exist_ok=True)
    ui.adb('pull', remote, destination, timeout=60)
    native = json.loads(args.native_report.read_text())['masks'][str(args.mask)]['native_sha256']
    contents = check_contents(args.original_apk, destination, native)
    record = {'mask': args.mask, 'runtime': runtime, 'runtime_log': lines[-1], 'ui_success': True,
              'apk_sha256': hashlib.sha256(destination.read_bytes()).hexdigest(), 'native_sha256': native,
              **contents}
    record['recovery'] = args.recovery
    (base.OUT_ROOT / f'case-{args.mask}-{args.runtime}{suffix}.json').write_text(json.dumps(record, indent=2) + '\n')
    print(json.dumps(record), flush=True)


if __name__ == '__main__':
    main()
