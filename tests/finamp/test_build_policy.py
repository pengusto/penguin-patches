"""Partial exploratory rebuilds cannot replace shipped native resources."""
from pathlib import Path
import json
import unittest

from build_native_payloads import PRODUCTION, payload_output


class OutputPolicyTests(unittest.TestCase):
    def test_partial_defaults_to_private_work_directory(self):
        self.assertEqual(payload_output([2], None, Path('/tmp/finamp-test')),
                         Path('/tmp/finamp-test/partial-payloads').resolve())

    def test_partial_rejects_production_and_descendants(self):
        for target in [PRODUCTION, PRODUCTION / 'arm64-v8a']:
            with self.assertRaises(ValueError):
                payload_output([2], target, Path('/tmp/finamp-test'))

    def test_old_seven_mask_matrix_is_partial_now(self):
        with self.assertRaises(ValueError):
            payload_output(range(1, 8), PRODUCTION, Path('/tmp/finamp-test'))

    def test_source_matrix_contains_every_independent_combination(self):
        matrix = json.loads(Path(__file__).with_name('native-sources.json').read_text())
        bits = [1, 2, 4, 8, 16]
        self.assertEqual(set(map(int, matrix['mask_bits'])), set(bits))
        self.assertEqual(set(map(int, matrix['masks'])), set(range(32)))
        for mask in range(32):
            entry = matrix['masks'][str(mask)]
            expected = {feature for bit in bits if mask & bit
                        for feature in matrix['masks'][str(bit)]['features']}
            self.assertEqual(set(entry['features']), expected, mask)
            self.assertRegex(entry['commit'], r'^[0-9a-f]{40}$')
            self.assertRegex(entry['tree'], r'^[0-9a-f]{40}$')
            self.assertEqual(entry['branch'], f'penguin/finamp-native-1.0.1-mask-{mask}')

    def test_complete_matrix_can_replace_production(self):
        self.assertEqual(payload_output(range(1, 32), None, Path('/tmp/finamp-test')),
                         PRODUCTION.resolve())
