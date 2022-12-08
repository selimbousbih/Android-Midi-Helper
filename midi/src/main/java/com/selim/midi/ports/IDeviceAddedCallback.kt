package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo

interface IDeviceAddedCallback {
    fun onDeviceAdded(deviceInfo: MidiDeviceInfo)
}
