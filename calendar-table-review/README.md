# Calendar table fragment regression

Official Fossify Calendar 1.12.0: previous Morphe patch leaves wrapperless table fragments raw in day list and editor; updated patch displays separated cell text. Native screenshots, same synthetic event, same emulator/1080x1920. They compare the previous and revised Morphe implementations, not a full table layout. 13 parser assertions pass and a title-only UI save retains exact source.

The equivalent source change for pengusto/Calendar PR #1 is separately built, linted and checked by Detekt; 25 Android helper/integration tests pass, including exact table-source retention. These screenshots were taken from the Morphe patched official APK, not that source build. Live Outlook sync is unverified.
