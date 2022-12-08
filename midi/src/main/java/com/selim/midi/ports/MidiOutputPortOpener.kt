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
) : IMidiOutputPortOpen, IDeviceRemovedCallback {
    private var openDevice: MidiDevice? = null
    private var outputPort: MidiOutputPort? = null

    override fun open(info: MidiDeviceInfo, portIndex: Int, receiver: MidiReceiver) {
        closeCurrentPort()

        midiManager.openDevice(info, { device ->
            openDevice = device?.also {
                outputPort = it.openOutputPort(portIndex)
                outputPort?.connect(receiver)
            }

            if (device == null) {
                Log.e(MidiConstants.TAG, "could not open $info")
            }
        }, null)
    }

    override fun onDeviceRemoved(deviceInfo: MidiDeviceInfo) {
        if (openDevice?.info == deviceInfo) {
            closeCurrentPort()
        }
    }

    override fun closeCurrentPort() {
        try {
            if (outputPort != null) {
                Log.i(MidiConstants.TAG, "MidiInputPortSelector.onClose() - close port")
                outputPort!!.close()
            }
            outputPort = null
            if (openDevice != null) {
                openDevice!!.close()
            }
            openDevice = null
        } catch (e: IOException) {
            Log.e(MidiConstants.TAG, "cleanup failed", e)
        }
    }
}