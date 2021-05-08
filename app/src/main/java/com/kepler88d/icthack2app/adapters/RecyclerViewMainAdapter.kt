package com.kepler88d.icthack2app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.databinding.ItemProjectBinding
import com.kepler88d.icthack2app.model.data.Project

class RecyclerViewMainAdapter (val context: Context, val list: List<Project>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ProjectItem(val binding: ItemProjectBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.textViewTitle.text = "${list[position].name} id: ${list[position].id}"
            binding.textViewDescription.text = list[position].description
            binding.cardViewProject.setOnClickListener {

            }
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

    override fun getItemCount() = list.size
}