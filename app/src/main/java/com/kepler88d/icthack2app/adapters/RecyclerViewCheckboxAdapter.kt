package com.kepler88d.icthack2app.adapters

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.databinding.ItemCheckboxGroupBinding
import com.kepler88d.icthack2app.databinding.ItemCheckboxSatelliteBinding
import com.kepler88d.icthack2app.databinding.ItemProjectBinding

class RecyclerViewCheckboxAdapter(val context: Context, val map: Map<String, List<String>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = mutableListOf<Item>()
    init {
        for(i in map.keys){
            list.add(Item(i, false,1))
            if (!map[i].isNullOrEmpty()){
                map[i]?.forEach {
                    list.add(Item(it, false,2))
                }
            }
        }
    }

    inner class GroupItem(val binding: ItemCheckboxGroupBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            if (binding.materialCheckBox.isChecked != list[position].checked){
                binding.materialCheckBox.performClick()
            }

            binding.materialCheckBox.text = list[position].name
            binding.materialCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("Debug checkbox", "click, $position, ")
                list[position].checked = isChecked
                var currPos = position

                while (currPos < list.size && (currPos == position || list[currPos].viewType != 1)){
                    list[currPos].checked = isChecked
                    Log.d("Debug checkbox", "aa: $currPos")
                    currPos++
                }
                val timer = object: CountDownTimer(1, 1) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() { notifyDataSetChanged() }
                }
                timer.start()

            }
        }
    }

    inner class SatelliteItem(val binding: ItemCheckboxSatelliteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            if (binding.materialCheckBox.isChecked != list[position].checked){
                binding.materialCheckBox.performClick()
            }
            binding.materialCheckBox.text = list[position].name
            binding.materialCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                var currPos = position-1
                while (list[currPos].viewType!=1){
                    currPos--
                }
                list[currPos].checked = false

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == 1){
            val binding = ItemCheckboxGroupBinding.inflate(inflater, parent, false)
            return GroupItem(binding)
        }
        else{
            val binding = ItemCheckboxSatelliteBinding.inflate(inflater, parent, false)
            return SatelliteItem(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list[position].viewType == 1){
            (holder as GroupItem).bind(position)
        }
        else{
            (holder as SatelliteItem).bind(position)
        }
    }

    override fun getItemCount(): Int {
        var countItems = 0
        map.forEach { t, u ->
            countItems+=u.size
        }
        return map.size+countItems
    }

    override fun getItemViewType(position: Int): Int = list[position].viewType

    data class Item(
        val name: String,
        var checked: Boolean,
        val viewType: Int
    )
}

