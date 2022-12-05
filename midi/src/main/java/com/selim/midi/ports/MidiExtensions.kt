package com.selim.midi.ports

import android.media.midi.MidiDevice
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiOutputPort
import com.selim.midi.model.MidiPortModel

fun Array<MidiDeviceInfo>.toMidiPortModels() = flatMap {
    it.ports.map { port -> MidiPortModel(it.id, port.portNumber, it.getName()) }
}

fun Array<MidiDeviceInfo>.toMidiPortModels(type: Int) = flatMap {
    it.ports
        .filter { port -> port.type == type }
        .map { port -> MidiPortModel(it.id, port.portNumber, it.getName()) }
}

fun Collection<MidiDeviceInfo>.toMidiPortModels() = flatMap {
    it.ports.map { port -> MidiPortModel(it.id, port.portNumber, port.name) }
}

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

