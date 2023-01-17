package com.selim.midimodule.ble

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.selim.midimodule.R

class BluetoothDevicesAdapter(private val dataSet: Array<BLEObject>) :
        RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textName)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_bluetooth_entry, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size

    data class BLEObject(val name: String, val address: String)
}
