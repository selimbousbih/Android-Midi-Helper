package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo

interface IDeviceRemovedCallback {
    fun onDeviceRemoved(deviceInfo: MidiDeviceInfo)
}
