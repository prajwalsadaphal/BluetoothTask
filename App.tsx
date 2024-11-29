import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  useColorScheme,
  View,
  NativeModules,
  PermissionsAndroid, 
  Platform,
  ToastAndroid
} from 'react-native';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';
import BluetoothStateManager from 'react-native-bluetooth-state-manager';



function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };


  const [isBluetoothActive,setIsBluetoothActive] = useState(false)

  const requestBluetoothPermissions = async () => {
    if (Platform.OS === 'android' && Platform.Version >= 31) {
      try {
        const granted = await PermissionsAndroid.requestMultiple([
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_ADVERTISE,
        ]);

        if (
          granted[PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN] === PermissionsAndroid.RESULTS.GRANTED &&
          granted[PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT] === PermissionsAndroid.RESULTS.GRANTED &&
          granted[PermissionsAndroid.PERMISSIONS.BLUETOOTH_ADVERTISE] === PermissionsAndroid.RESULTS.GRANTED
        ) {
          console.log('Bluetooth permissions granted');
        } else {
          console.error('Bluetooth permissions denied');
        }
      } catch (err) {
        console.warn(err);
      }
    }
  };


  const enableBluetooth = async () => {
    try {
      await NativeModules.BluetoothModule.enableBluetooth();
      checkBluetoothStatus()
    } catch (error) {
      console.error("error", JSON.stringify(error))
    }
  }

  const disableBluetooth = async () => {
    try {
      await NativeModules.BluetoothModule.disableBluetooth();
      checkBluetoothStatus()
    } catch (error) {
      console.error("error", JSON.stringify(error))
    }
  }

  const checkBluetoothStatus = async ()=>{
    BluetoothStateManager.getState().then((bluetoothState) => {
      switch (bluetoothState) {
        case 'PoweredOff':
          setIsBluetoothActive(false)
          ToastAndroid.show("Bluetooth Off",3000)
          break;

        case 'PoweredOn':
          setIsBluetoothActive(true)
          ToastAndroid.show("Bluetooth On",3000)
          break;

        default:
          break;
      }
    });
  }

  useEffect(() => {
    requestBluetoothPermissions()
  }, [])


  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <View style={styles.conatainerView}>
        {isBluetoothActive?
    
        <TouchableOpacity style={styles.button} onPress={() => disableBluetooth()}>
        <Text>Disable Bluetooth</Text>
      </TouchableOpacity>
       :
       <TouchableOpacity style={styles.buttonActive} onPress={() => enableBluetooth()}>
       <Text>Enable Bluetooth</Text>
     </TouchableOpacity>
      }
       
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  conatainerView: { 
      width: "100%", 
      height: "100%", 
      justifyContent: "center", 
      alignItems: "center" },
  button: {
    padding:10,
    backgroundColor:"#a4c0db",
    marginTop: 32,
    paddingHorizontal: 24,
  },
  textWhite:{

  },
  buttonActive: {
    padding:10,
    backgroundColor:"#4582ed",
    marginTop: 32,
    paddingHorizontal: 24,
  },
});

export default App;
