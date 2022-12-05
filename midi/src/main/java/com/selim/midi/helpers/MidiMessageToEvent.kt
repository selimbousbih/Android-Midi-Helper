package com.selim.midi.helpers

import com.selim.midi.definitions.MidiConstants
import com.selim.midi.definitions.MidiEvent
import kotlin.experimental.and

object MidiMessageToEvent {

    fun get(data: ByteArray, _offset: Int): MidiEvent {
        var offset = _offset
        val statusByte = data[offset++]
        val status = (statusByte and 0xFF.toByte())

        val numData = MidiConstants.getBytesPerMessage(statusByte) - 1

        val channel = (status and 0x0F).toInt() // if (status in 0x80..0xef) -> channel message

        val dataArray = Array(3) { 0 }
        for (i in 0 until numData) {
            dataArray[i] = data[offset++].toInt()
        }

        return MidiEvent(status, channel, dataArray[0], dataArray[1])
    }
}