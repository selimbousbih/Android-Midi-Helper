package com.selim.midi.ports

import com.selim.midi.definitions.MidiEvent


interface IMidiEventSender {
    fun send(event: MidiEvent)
}