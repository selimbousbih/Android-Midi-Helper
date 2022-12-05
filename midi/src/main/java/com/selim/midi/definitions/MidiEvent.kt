package com.selim.midi.definitions

open class MidiEvent(
    val type: Byte = 0,
    val channel: Int = 0,
    val v1: Int = 0,
    val v2: Int = 0,
    val offset: Int = 0
) {

    override fun toString(): String {
        return "MidiEvent [type: $type  |  v1: $v1  |  v2: $v2]"
    }
}
