package com.kepler88d.icthack2app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.databinding.ItemProjectBinding

class RecyclerViewMainAdapter (val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ProjectItem(val binding: ItemProjectBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.textviewProjectName.text = "Project name $position"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProjectBinding.inflate(inflater, parent, false)
        return ProjectItem(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProjectItem).bind(position)
    }

    override fun getItemCount() = 200



}