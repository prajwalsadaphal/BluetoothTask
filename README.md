### In Android API 33+ (13+) enabling/disabling bluetooth without user consent is not more possible 
official doc link
https://developer.android.com/reference/android/bluetooth/BluetoothAdapter#enable()

used below solution
- Prompt the user with an intent for explicit permission from where user can turn-on bluetooth.
- Similarly, there is no built-in intent for disabling Bluetooth, so you should guide users to disable it manually.

###setup
- install apk and allow all the permission


###Code flow
- All RN code is present in App.tsx
- By calling requestBluetoothPermissions in useEffect we get all the required persmisson
- Created Native module BluetoothModule.kt in android for user intent and enabling bluetooth. Then native modules are invoked from App.tsx on button press below are the functions present in App.tsx
```
enableBluetooth
disableBluetooth
```
- used package react-native-bluetooth-state-manager for checking bluetooth state on/off in below function checkBluetoothStatus so we can toggle button and show Toast button.



