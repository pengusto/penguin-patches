# Display emails and notes from merged contact sources

Include duplicate-source email entries and nonempty distinct notes in detail rendering while merge is enabled. Preserve original contact records, source editing, labels and visibility. Organization and full-contact share/export are outside this focused change.

Build/Lint/Detekt pass; native Android rendering test verifies duplicate email suppression, separate notes, merge off and source model immutability. Test creates and removes its own synthetic raw contact.

Closes #327
