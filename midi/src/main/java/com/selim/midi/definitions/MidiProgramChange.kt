package com.selim.midi.definitions

import com.selim.midi.definitions.MidiConstants.STATUS_PROGRAM_CHANGE

class MidiProgramChange(program: Int, channel: Int = 0) : MidiEvent(
    STATUS_PROGRAM_CHANGE, channel, program) {

    companion object{
        const val ACOUSTIC_GRAND_PIANO = 0
        const val GUITAR_OVERDRIVE = 29
        const val GUITAR_DISTORTION = 30
        const val SITAR = 104
        const val VIOLIN = 40
        const val STRINGS = 48
        const val SAXOPHONE = 66
        const val FLUTE = 73
        const val FIDDLE = 110
    }
}
