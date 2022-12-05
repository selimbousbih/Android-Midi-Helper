package com.selim.midi.definitions

import com.selim.midi.definitions.MidiConstants.STATUS_NOTE_OFF

class MidiNoteOff(note: Int, velocity: Int = 0, channel: Int = 0) : MidiEvent(
    STATUS_NOTE_OFF, channel, note, velocity)
