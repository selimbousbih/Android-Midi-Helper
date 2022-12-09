package com.selim.midi.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.selim.midi.model.DevicePortWrapper
import com.selim.midi.ports.getName


class MidiDevicePortArrayAdapter(context: Context, val data: ArrayList<DevicePortWrapper>, itemLayoutId: Int) :
    ArrayAdapter<DevicePortWrapper>(context, itemLayoutId, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val view = recycledView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_dropdown_item,
            parent,
            false
        )

        getItem(position)?.apply {
            view.findViewById<TextView>(android.R.id.text1).text = deviceInfo.getName()
        }

        return view
    }

    fun updateData(items: List<DevicePortWrapper>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }
}