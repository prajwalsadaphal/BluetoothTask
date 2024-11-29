### In Android API 33+ (13+) enabling/disabling bluetooth programmatically is not more possible check below official doc link. we need to get user consent for enabling bluetooth implemeted solution in same way
https://developer.android.com/reference/android/bluetooth/BluetoothAdapter#enable()
 



used below solution
- Prompt the user with an intent for explicit permission from where user can turn-on bluetooth.
- Similarly, there is no built-in intent for disabling Bluetooth, so you should guide users to disable it manually.

### setup
- to run project run below commands in root of project
```
npm install
npm run android
```

### app demo
- install apk shared on drive and allow all the permission


### Code flow
- All RN code is present in App.tsx
- By calling requestBluetoothPermissions in useEffect we get all the required persmisson
- Created Native module BluetoothModule.kt in android for user intent and enabling bluetooth. Then native modules are invoked from App.tsx on button press below are the functions present in App.tsx where we call NativeModule.
```
enableBluetooth
disableBluetooth
```
- NativeModule BluetoothModule.kt code can be found in same location as MainApplication.java
- in native modudule using BluetoothAdapter and BluetoothManager we enable/disable bluetooth here I created intent which is required for enabling api.
- used package react-native-bluetooth-state-manager for checking bluetooth state on/off in below function checkBluetoothStatus so we can toggle button and show Toast button.



