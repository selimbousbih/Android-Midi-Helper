package com.selim.midi.ports

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.media.midi.MidiOutputPort
import android.media.midi.MidiReceiver
import android.util.Log
import com.selim.midi.definitions.MidiConstants
import java.io.IOException

class MidiOutputPortOpener(
    private val midiManager: MidiManager
) : IMidiOutputPortOpen {
    private var mOpenDevice: MidiDevice? = null
    private var mOutputPort: MidiOutputPort? = null

    override fun open(info: MidiDeviceInfo, portIndex: Int, receiver: MidiReceiver) {
        closeCurrentPort()

        midiManager.openDevice(info, { device ->
            mOpenDevice = device?.also {
                mOutputPort = it.openOutputPort(portIndex)
                mOutputPort?.connect(receiver)
            }

            if (device == null) {
                Log.e(MidiConstants.TAG, "could not open $info")
            }
        }, null)
    }

    override fun closeCurrentPort() {
        try {
            if (mOutputPort != null) {
                Log.i(MidiConstants.TAG, "MidiInputPortSelector.onClose() - close port")
                mOutputPort!!.close()
            }
            mOutputPort = null
            if (mOpenDevice != null) {
                mOpenDevice!!.close()
            }
            mOpenDevice = null
        } catch (e: IOException) {
            Log.e(MidiConstants.TAG, "cleanup failed", e)
        }
    }
}