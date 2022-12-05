package com.selim.midi.helpers

import android.media.midi.MidiReceiver
import java.util.concurrent.TimeUnit

/**
 * Convert incoming MIDI messages to a string and write them to a ScopeLogger.
 * Assume that messages have been aligned using a MidiFramer.
 */
class ExampleReceiver : MidiReceiver() {
    private val mStartTime: Long = System.nanoTime()

    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {}

    companion object {
        const val TAG = "MidiScope"
        private val NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1)
    }
}
