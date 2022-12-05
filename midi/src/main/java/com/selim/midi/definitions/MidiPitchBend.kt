package com.selim.midi.definitions

import com.selim.midi.definitions.MidiConstants.STATUS_PITCH_BEND

class MidiPitchBend(v1: Int, v2: Int, channel: Int = 0) : MidiEvent(
    STATUS_PITCH_BEND, channel, v1, v2)
