# Finamp #1689 / #1790 verification

Acceptance in progress on this draft branch. Source and Desktop checks below
are complete; the actual Android Manager exports and matched APK captures will
be added before the change is marked ready or released.

- Official original Finamp 1.0.1 universal SHA-256:
  `0fc61e5cb80d37b10a735851c2217cbc855b3f1a04bde577187658531ffef581`.
- Complete local candidate MPP SHA-256:
  `79ceddd2405bee3464acbde4fa9a9407f413136ec42392b90955c5d64998b5f0`.
- All 31 PFD outputs replay correctly and apply through actual Morphe Desktop
  1.19.1/Patcher 2.0.1. Every retained entry except ARM64 libapp.so is unchanged;
  other native architectures are removed.
- Four unsupported-input tests fail with selected-patch attribution, nonzero
  status and no output APK: app library, engine, missing ARM64 and forced version.
- Source tests: 29 for PR1834, 31 for PR1835, 66 in the combined five-fix native
  tree; targeted source analysis is clean. Nine codec/output/matrix policy tests
  and 13 fixture/source-decoder tests pass.
- All 32 complete public source branches/trees, including mask zero, and both
  new source feature predicates match the pinned matrix.

Only synthetic fixtures and disposable emulator state are used. No APK,
signing key, account credentials or private fixture control token belongs here.
