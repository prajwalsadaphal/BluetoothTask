package com.bluetoothapp

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ActivityEventListener
import android.content.pm.PackageManager

class BluetoothModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), ActivityEventListener {

    private val bluetoothAdapter: BluetoothAdapter?
    private var enablePromise: Promise? = null
    private var disablePromise: Promise? = null

    init {
        val bluetoothManager = reactContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String {
        return "BluetoothModule"
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(reactApplicationContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    @ReactMethod
    fun enableBluetooth(promise: Promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                promise.reject("PERMISSION_ERROR", "BLUETOOTH_CONNECT permission is required")
                return
            }
        }

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            enablePromise = promise
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            currentActivity?.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH)
        } else if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            promise.resolve("Bluetooth is already enabled")
        } else {
            promise.reject("ERROR", "Bluetooth is not supported on this device")
        }
    }

    @ReactMethod
    fun disableBluetooth(promise: Promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                promise.reject("PERMISSION_ERROR", "BLUETOOTH_CONNECT permission is required")
                return
            }
        }

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
           disablePromise = promise
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setClassName(
                "com.android.settings",
                "com.android.settings.bluetooth.BluetoothSettings"
            )
            currentActivity?.startActivityForResult(intent, REQUEST_DISABLE_BLUETOOTH)
        } else if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            promise.resolve("Bluetooth is already disabled")
        } else {
            promise.reject("ERROR", "Bluetooth is not supported on this device")
        }
    }

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ENABLE_BLUETOOTH -> {
                if (resultCode == Activity.RESULT_OK) {
                    enablePromise?.resolve("Bluetooth enabled")
                } else {
                    enablePromise?.reject("ERROR", "Bluetooth enabling was canceled by user")
                }
                enablePromise = null
            }
            REQUEST_DISABLE_BLUETOOTH -> {
                disablePromise?.resolve("Bluetooth settings opened to disable manually")
                disablePromise = null
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        
    }

    companion object {
        private const val REQUEST_ENABLE_BLUETOOTH = 1
        private const val REQUEST_DISABLE_BLUETOOTH = 2
    }
}
