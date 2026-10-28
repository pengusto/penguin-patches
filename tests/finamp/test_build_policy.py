"""Partial exploratory rebuilds cannot replace shipped native resources."""
from pathlib import Path
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

    def test_complete_matrix_can_replace_production(self):
        self.assertEqual(payload_output(range(1, 8), None, Path('/tmp/finamp-test')),
                         PRODUCTION.resolve())
