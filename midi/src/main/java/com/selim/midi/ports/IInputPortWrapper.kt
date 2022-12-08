package com.selim.midi.ports

import android.media.midi.MidiInputPort

interface IInputPortWrapper {
    fun getOpenInputPort(): MidiInputPort?

    fun selectedPortId(): Int
}
