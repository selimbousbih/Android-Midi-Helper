package com.selim.midimodule

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.common.midi.MidiEventThread
import com.selim.midi.helpers.createMidiFramerForReceiver
import com.selim.midi.ports.*
import com.selim.midimodule.ui.MidiPortSelector
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var midiManager: MidiManager
    private lateinit var midiDeviceSelection: MidiDeviceSelection
    private lateinit var midiOutputPortOpener: MidiOutputPortOpener
    private lateinit var midiInputPortOpener: MidiInputPortOpener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        midiManager = getSystemService(MIDI_SERVICE) as MidiManager
        midiDeviceSelection = MidiDeviceSelection(midiManager)
        midiOutputPortOpener = MidiOutputPortOpener(midiManager)
        midiInputPortOpener = MidiInputPortOpener(midiManager)

        val midiPortSelectorView = findViewById<MidiPortSelector>(R.id.midiPortSelector)

        val devices = midiDeviceSelection.deviceInfoUpdates

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                devices.onEach { devices ->
                    midiPortSelectorView.render(
                        MidiPortSelector.ViewState(
                            devices.toMidiPortModels(
                                MidiDeviceInfo.PortInfo.TYPE_INPUT
                            )
                        )
                    )

                    midiPortSelectorView.setOnMidiPortSelectedListener { model ->
                        val selectedDevice = devices.first { it.id == model.deviceId }
                        midiOutputPortOpener.open(selectedDevice,
                            model.portNumber, createMidiFramerForReceiver { bytes, offset, count, timestamp -> })

                        midiInputPortOpener.open(
                            selectedDevice, model.portNumber
                        )
                    }
                }.collect()
            }
        }

    }

    fun example() {
        val devices = midiDeviceSelection.deviceInfoUpdates.value

        val selectedDevice = devices.first { it.properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER) != "MuseLead" }

        val thread = MidiEventThread().apply { start() }
        midiOutputPortOpener.open(selectedDevice, selectedDevice.getOutputPorts().first().portNumber, thread.receiver)

        thread.sender.connect(
            createMidiFramerForReceiver { bytes, offset, count, timestamp ->
                val msg = MidiPrinter.formatMessage(bytes, offset)
                println("Received event! $msg")
            }
        )
    }

    fun setupSpinner(spinner: Spinner) {
        val devices = midiDeviceSelection.deviceInfoUpdates.value

        val uiDevices = devices.toMidiPortModels()

        val adapter = MidiDeviceArrayAdapter(this, ArrayList(), -1).also {
            spinner.adapter = it

            it.updateData(uiDevices)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                // updateSpinnerSelectedText()

                val item = adapter.getItem(position) ?: return
                val device = devices.first { it.id == item.deviceId } ?: return

                midiOutputPortOpener.open(device, item.portNumber, createMidiFramerForReceiver { bytes, i, i2, l -> })
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }
}