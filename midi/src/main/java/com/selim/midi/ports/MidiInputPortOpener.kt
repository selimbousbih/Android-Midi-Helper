package com.selim.midi.ports

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.selim.midi.definitions.MidiConstants
import java.io.IOException

class MidiInputPortOpener(
    private val midiManager: MidiManager
) : IMidiInputPortOpen, IInputPortWrapper, IDeviceRemovedCallback {
    private var openDevice: MidiDevice? = null
    private var inputPort: MidiInputPort? = null

    override fun open(
        info: MidiDeviceInfo, portIndex: Int,
        onPortOpenResult: (MidiInputPort?) -> Unit
    ) {
        closeCurrentPort()

        midiManager.openDevice(info, { device ->
            openDevice = device?.also {
                inputPort = it.openInputPortOrHandleError(portIndex) {
                    Log.e(MidiConstants.TAG, "Could not open input port on $info")
                }
                onPortOpenResult(inputPort)
            }

            if (device == null) {
                Log.e(MidiConstants.TAG, "Could not open $info")
            }
        }, Handler(Looper.getMainLooper()))
    }

    override fun getOpenInputPort(): MidiInputPort? {
        return inputPort
    }

    override fun selectedPortId(): Int = (openDevice?.info?.id ?: 0) + (inputPort?.portNumber ?: 0)

    override fun onDeviceRemoved(deviceInfo: MidiDeviceInfo) {
        if (openDevice?.info == deviceInfo) {
            closeCurrentPort()
        }
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
