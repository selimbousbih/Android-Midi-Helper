package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.media.midi.MidiManager.DeviceCallback
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MidiDeviceSelection(
    private val midiManager: MidiManager,
) : DeviceCallback() {

    private val job = Job()
    private val coroutineScope: CoroutineScope = CoroutineScope(job + Dispatchers.Default)

    private val _deviceInfoUpdates = MutableStateFlow(midiManager.devices)
    val deviceInfoUpdates = _deviceInfoUpdates.asStateFlow()

    init {
        midiManager.registerDeviceCallback(
            this,
            Handler(Looper.getMainLooper())
        )
    }

    override fun onDeviceAdded(device: MidiDeviceInfo?) {
        super.onDeviceAdded(device)
        coroutineScope.launch { _deviceInfoUpdates.emit(midiManager.devices) }
    }

    override fun onDeviceRemoved(device: MidiDeviceInfo?) {
        super.onDeviceRemoved(device)
        coroutineScope.launch { _deviceInfoUpdates.emit(midiManager.devices) }
    }
}


