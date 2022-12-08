package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort

interface IMidiInputPortOpen {
    fun open(info: MidiDeviceInfo, portIndex: Int, onPortOpenResult: (MidiInputPort?) -> Unit = {})

    fun closeCurrentPort()
}