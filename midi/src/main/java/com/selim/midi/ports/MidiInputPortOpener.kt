package com.selim.midi.ports

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.util.Log
import com.selim.midi.definitions.MidiConstants
import java.io.IOException

class MidiInputPortOpener(
    private val midiManager: MidiManager
) : IMidiInputPortOpen {
    private var openDevice: MidiDevice? = null
    var inputPort: MidiInputPort? = null

    override fun open(
        info: MidiDeviceInfo, portIndex: Int,
        onPortOpenSuccess: (MidiInputPort) -> Unit
    ) {
        closeCurrentPort()

        midiManager.openDevice(info, { device ->
            openDevice = device?.also {
                inputPort = it.openInputPortOrHandleError(portIndex, onPortOpenSuccess) {
                    Log.e(MidiConstants.TAG, "could not open input port on $info")
                }
            }

            if (device == null) { Log.e(MidiConstants.TAG, "could not open $info") }
        }, null)
    }

    override fun closeCurrentPort() {
        try {
            if (inputPort != null) {
                Log.i(MidiConstants.TAG, "MidiInputPortSelector.onClose() - close port")
                inputPort!!.close()
            }
            inputPort = null
            if (openDevice != null) {
                openDevice!!.close()
            }
            openDevice = null
        } catch (e: IOException) {
            Log.e(MidiConstants.TAG, "cleanup failed", e)
        }
    }
}