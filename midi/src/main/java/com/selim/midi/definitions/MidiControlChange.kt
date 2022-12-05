package com.selim.midi.definitions

import com.selim.midi.definitions.MidiConstants.STATUS_CONTROL_CHANGE

class MidiControlChange(control: Int, value: Int, channel: Int = 0) : MidiEvent(
    STATUS_CONTROL_CHANGE, channel, control, value)
