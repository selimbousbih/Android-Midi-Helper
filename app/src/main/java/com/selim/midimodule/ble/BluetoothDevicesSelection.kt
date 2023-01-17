package com.selim.midimodule.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BluetoothDevicesSelection(
    private val bluetoothAdapter: BluetoothAdapter
) {
    private val job = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(job + Dispatchers.Default)

    private val _deviceUpdates = MutableStateFlow(emptySet<BluetoothDevice>())
    val deviceInfoUpdates = _deviceUpdates.asStateFlow()

    fun refresh() {
        coroutineScope.launch {
            _deviceUpdates.emit(bluetoothAdapter.bondedDevices)
        }
    }
}