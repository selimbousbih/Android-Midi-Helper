package com.selim.midi.ports

import com.selim.midi.definitions.MidiEvent

class MidiEventSender(
    private val portWrapper: IInputPortWrapper
) : IMidiEventSender {

    override fun send(event: MidiEvent) {
        val type = (event.type + event.channel.toByte()).toByte()
        val buffer = ByteArray(32)
        var numBytes = 0
        buffer[numBytes++] = type
        buffer[numBytes++] = event.v1.toByte()
        buffer[numBytes] = event.v2.toByte()
        val offset = 0

        try {
            portWrapper.getOpenInputPort().send(buffer, offset, numBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
