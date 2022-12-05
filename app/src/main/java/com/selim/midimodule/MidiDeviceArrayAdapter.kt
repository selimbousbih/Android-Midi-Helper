package com.selim.midimodule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.selim.midi.model.MidiPortModel


class MidiDeviceArrayAdapter(context: Context, val data: ArrayList<MidiPortModel>, itemLayoutId: Int) :
    ArrayAdapter<MidiPortModel>(context, itemLayoutId, data) {

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
            view.findViewById<TextView>(android.R.id.text1).text = name
        }

        return view
    }

    fun getDeviceIdAt(position: Int): Int {
        if (position !in data.indices)
            return -1000
        return data[position].deviceId
    }

    fun updateData(items: List<MidiPortModel>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun getPositionOfId(id: Int): Int {
        return data.find { it.portNumber == id }?.portNumber ?: -1
    }
}