package com.selim.midi.ports

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort

interface IInputPortWrapper {
    fun getOpenInputPort(): MidiInputPort?

    fun getOpenDeviceInfo(): MidiDeviceInfo?
}
