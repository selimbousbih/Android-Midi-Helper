package com.selim.midi.ports

import android.media.midi.MidiInputPort
import android.media.midi.MidiOutputPort

interface IPortWrapper {
    fun getOpenInputPort(): MidiInputPort

    fun getOpenOutputPort(): MidiOutputPort
}