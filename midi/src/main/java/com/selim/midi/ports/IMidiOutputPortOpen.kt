package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiReceiver

interface IMidiOutputPortOpen {
    fun open(info: MidiDeviceInfo, portIndex: Int, receiver: MidiReceiver)

    fun closeCurrentPort()
}