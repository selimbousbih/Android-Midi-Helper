package com.selim.midi.ui

import android.content.Context
import android.media.midi.MidiDeviceInfo
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.selim.midi.R
import com.selim.midi.model.DevicePortWrapper

open class MidiPortSelector
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    open val spinnerLayout: Int = R.layout.spinner_item

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_midi_port_selector, this, true)
    }

    fun render(state: ViewState) {
        val spinner = findViewById<Spinner>(R.id.inputPortSpinner)

        spinner.adapter = MidiDevicePortArrayAdapter(context, ArrayList(state.ports), spinnerLayout)
    }

    fun setOnMidiPortSelectedListener(block: (DevicePortWrapper) -> Unit) {
        spinner().apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val adapter = adapter as? MidiDevicePortArrayAdapter ?: return
                    adapter.getItem(position)?.let(block::invoke)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {}
            }
        }
    }

    fun getSelectedItem(): DevicePortWrapper? = spinner().selectedItem as? DevicePortWrapper

    fun setSelectedSpinnerItem(position: Int) = spinner().setSelection(position)

    fun positionOfDevice(deviceInfo: MidiDeviceInfo): Int {
        val adapter = spinner().adapter as? MidiDevicePortArrayAdapter ?: return 0
        return adapter.data.indexOfFirst { it.deviceInfo == deviceInfo }
    }

    private fun spinner() = findViewById<Spinner>(R.id.inputPortSpinner)

    class ViewState(
        val ports: List<DevicePortWrapper>
    )
}
