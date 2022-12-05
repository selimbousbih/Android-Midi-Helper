package com.selim.midi.helpers

import android.media.midi.MidiReceiver
import com.example.android.common.midi.MidiFramer

/**
 * Creates a [MidiFramer] receiver wrapping actual receiver that calls the specified block when onSend is called
 * MidiFramer converts a stream of arbitrary MIDI bytes into discrete messages.
 * @param block (bytes, offset, count, timestamp)
 */
fun createMidiFramerForReceiver(block: (ByteArray?, Int, Int, Long) -> Unit): MidiReceiver {
    val receiver = object : MidiReceiver() {
        override fun onSend(msg: ByteArray?, offset: Int, count: Int, timestamp: Long) {
            block(msg, offset, count, timestamp)
        }
    }

    return MidiFramer(receiver)
}
