package com.selim.midi.ports

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiOutputPort
import com.selim.midi.model.DevicePortWrapper


fun Collection<DevicePortWrapper>.withType(type: Int) = filter {
    it.portInfo.type == type
}

fun Collection<DevicePortWrapper>.inputOnly() = filter {
    it.portInfo.type == MidiDeviceInfo.PortInfo.TYPE_INPUT
}

fun Array<MidiDeviceInfo>.toDevicePortModels() = flatMap {
    it.ports.map { port -> DevicePortWrapper(it, port) }
}

fun MidiDeviceInfo.toDevicePortModels(): List<DevicePortWrapper>
    = ports.map { port -> DevicePortWrapper(this, port) }

fun Array<MidiDeviceInfo>.getInputPorts() = flatMap { it.getInputPorts() }

fun Collection<MidiDeviceInfo>.getInputPorts() = flatMap { it.getInputPorts() }

fun MidiDeviceInfo.getInputPorts() = ports.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_INPUT }

fun MidiDeviceInfo.getOutputPorts() = ports.filter { it.type == MidiDeviceInfo.PortInfo.TYPE_OUTPUT }

fun MidiDeviceInfo.getName(): String {
    val sb = StringBuilder()
    sb += properties.getString(MidiDeviceInfo.PROPERTY_NAME)
        ?: (properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
                + ", " + properties.getString(MidiDeviceInfo.PROPERTY_PRODUCT))
    return sb.toString()
}

operator fun StringBuilder.plusAssign(string: String?) {
    append(string)
}

fun MidiDevice.openInputPortOrHandleError(
    portIndex: Int,
    success: (MidiInputPort) -> Unit = {},
    block: () -> Unit = {}
): MidiInputPort? {
    return openInputPort(portIndex).also { if (it == null) block.invoke() else success.invoke(it) }
}

fun MidiDevice.openOutputPortOrHandleError(
    portIndex: Int,
    block: () -> Unit = {}
): MidiOutputPort? {
    return openOutputPort(portIndex).also { if (it == null) block.invoke() }
}

