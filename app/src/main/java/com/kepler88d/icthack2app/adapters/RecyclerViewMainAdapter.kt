package com.kepler88d.icthack2app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.activities.IOnBackPressed
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.activities.currentOpened
import com.kepler88d.icthack2app.databinding.ItemProjectBinding
import com.kepler88d.icthack2app.fragments.IOnBackPressed2
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.model.data.Project

class RecyclerViewMainAdapter(val activity: MainActivity, val list: List<Project>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), IOnBackPressed2 {
    var currentOpenProjIndex = -1
    lateinit var tmp : ItemProjectBinding

    inner class ProjectItem(val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.cardViewProject.setOnClickListener {
                tmp = binding
                currentOpened = MainActivity.OPENED_PROJECT_SCREEN
                activity.performTransformAnimation(
                    binding.cardViewProject,
                    activity.binding.projectScreen.root
                )

//                binding.textViewSpecializations.text
                currentOpenProjIndex = position
                activity.fillProjectInfo(list[position])
                activity.binding.fabAddProject.visibility = View.GONE

                activity.binding.projectScreen.buttonBack.setOnClickListener {

                    activity.performTransformAnimation(
                        activity.binding.projectScreen.root,
                        binding.cardViewProject
                    )
                    activity.binding.fabAddProject.visibility = View.VISIBLE
                }
            }

            if(position == list.size) {
                binding.cardViewProject.visibility = View.INVISIBLE
                binding.cardViewProject.isClickable = false
                return
            }
            else{
                binding.cardViewProject.visibility = View.VISIBLE
                binding.cardViewProject.isClickable = true
            }

            binding.textViewTitle.text = list[position].name
            binding.textViewDescription.text = list[position].description
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

    override fun getItemCount() = list.size + 1

    override fun onBackPressed() {
        Log.d("sdzf", "click back adapter")
        activity.performTransformAnimation(
            activity.binding.projectScreen.root,
            tmp.cardViewProject
        )
        activity.binding.fabAddProject.visibility = View.VISIBLE
    }
}