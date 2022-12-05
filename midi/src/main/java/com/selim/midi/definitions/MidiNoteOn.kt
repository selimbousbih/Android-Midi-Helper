package com.selim.midi.definitions

import com.selim.midi.definitions.MidiConstants.STATUS_NOTE_ON

class MidiNoteOn(note: Int, velocity: Int, channel: Int = 0) : MidiEvent(
    STATUS_NOTE_ON, channel, note, velocity)
