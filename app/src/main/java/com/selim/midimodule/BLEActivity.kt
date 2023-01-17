package com.selim.midimodule

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDevice
import android.media.midi.MidiManager
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.selim.midi.definitions.MidiNoteOn
import com.selim.midi.ports.*
import com.selim.midimodule.ble.BluetoothDevicesSelection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class BLEActivity : AppCompatActivity(), MidiManager.OnDeviceOpenedListener {

    private lateinit var midiManager: MidiManager
    private lateinit var midiInputPortOpener: MidiInputPortOpener
    private lateinit var bluetoothDevicesSelection: BluetoothDevicesSelection
    private lateinit var midiEventSender: IMidiEventSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ble)

        midiManager = getSystemService(MIDI_SERVICE) as MidiManager
        midiInputPortOpener = MidiInputPortOpener(midiManager)
        midiEventSender = MidiEventSender(midiInputPortOpener)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            return
        }

        bluetoothDevicesSelection = BluetoothDevicesSelection(bluetoothAdapter)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bluetoothDevicesSelection.deviceInfoUpdates.onEach { pairedDevices ->
                    println("found ${pairedDevices.size} paired devices")

                    pairedDevices.forEach { device ->
                        if (device.name.contains("think-it")) {
                            midiManager.openBluetoothDevice(device, this@BLEActivity, Handler(mainLooper));
                        }
                    }
                }.collect()
            }
        }

        runWithPermissionCheck(BLUETOOTH_CONNECT) {
            bluetoothDevicesSelection.refresh()
        }

        val button = findViewById<Button>(R.id.buttonSend)
        button.setOnClickListener {
            midiEventSender.send(MidiNoteOn(60, 60))
        }

        //val bluetoothMidiService = BluetoothMidiService(bluetoothAdapter)
    }

    private fun runWithPermissionCheck(permission: String, block: () -> Unit) {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    block()
                } else {
                    // Explain to the user that the feature is unavailable
                }
            }

        when {
            checkSelfPermission(BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED -> {
                block()
            }
            shouldShowRequestPermissionRationale(BLUETOOTH_CONNECT) -> {
                // Explain
            }
            else -> {
                requestPermissionLauncher.launch(BLUETOOTH_CONNECT)
            }
        }
    }

    override fun onDeviceOpened(device: MidiDevice) {
        println("BLEActivity.onDeviceOpened: ${device.info.getName()}")

        device.info
            .toDevicePortModels()
            .inputOnly()
            .first()
            .let {
                midiInputPortOpener.openFromDevice(device, it.portInfo.portNumber) {
                    println("Port opened success!")
                }
            }
    }
}