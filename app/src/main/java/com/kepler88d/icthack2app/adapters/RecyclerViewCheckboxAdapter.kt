package com.kepler88d.icthack2app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.databinding.ItemCheckboxGroupBinding
import com.kepler88d.icthack2app.databinding.ItemCheckboxSatelliteBinding

class RecyclerViewCheckboxAdapter(private val map: Map<String, List<String>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = mutableListOf<Item>()

    init {
        for (i in map.keys) {
            list.add(Item(i, false, 1))
            if (!map[i].isNullOrEmpty()) {
                map[i]!!.forEach {
                    list.add(Item(it, false, 2))
                }
            }
        }
    }

    inner class GroupItem(val binding: ItemCheckboxGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.materialCheckBox.text = list[position].name
            binding.materialCheckBox.isChecked = list[position].checked
            binding.materialCheckBox.setOnClickListener {
                var currPos = position
                while (currPos < list.size && (position == currPos || list[currPos].viewType != 1)) {
                    list[currPos].checked = binding.materialCheckBox.isChecked
                    notifyDataSetChanged()
                    currPos++
                }
            }
        }
    }

    inner class SatelliteItem(val binding: ItemCheckboxSatelliteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.materialCheckBox.text = list[position].name
            binding.materialCheckBox.isChecked = list[position].checked
            binding.materialCheckBox.setOnClickListener {
                list[position].checked = binding.materialCheckBox.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            val binding = ItemCheckboxGroupBinding.inflate(inflater, parent, false)
            GroupItem(binding)
        } else {
            val binding = ItemCheckboxSatelliteBinding.inflate(inflater, parent, false)
            SatelliteItem(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == 1) {
            (holder as GroupItem).bind(position)
        } else {
            (holder as SatelliteItem).bind(position)
        }
    }

    override fun getItemCount(): Int = map.size + map.values.map { it.size }.sum()

    override fun getItemViewType(position: Int): Int = list[position].viewType

    data class Item(
        val name: String,
        var checked: Boolean,
        val viewType: Int
    )
}

