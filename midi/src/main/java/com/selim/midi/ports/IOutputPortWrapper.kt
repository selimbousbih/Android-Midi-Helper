package com.selim.midi.ports

import android.media.midi.MidiOutputPort

interface IOutputPortWrapper {
    fun getOpenOutputPort(): MidiOutputPort
}