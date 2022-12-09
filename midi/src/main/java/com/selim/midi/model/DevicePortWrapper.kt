package com.selim.midi.model

import android.media.midi.MidiDeviceInfo

class DevicePortWrapper (
    val deviceInfo: MidiDeviceInfo,
    val portInfo: MidiDeviceInfo.PortInfo
)
