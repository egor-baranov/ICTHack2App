package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.adapters.RecyclerViewMainAdapter
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.databinding.ItemProjectBinding
import com.kepler88d.icthack2app.model.Project
import com.kepler88d.icthack2app.model.RequestWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class MainFragment : Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding
    lateinit var adapter: RecyclerViewMainAdapter
    lateinit var projectsList: MutableList<Project>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchProjects()


    }

    private fun fetchProjects() {
        CoroutineScope(Dispatchers.IO).launch {



//            val str : String = RequestWorker().getProjectsList()
//            val a = JSONArray(str)

//            for (i in 0 until a.length()){
//                val b: Project = Project(
//                    JSONObject(a[i]).getInt("id"),
//                    JSONObject(a[i]).getString("id"),
//                    JSONObject(a[i]).getString("id"),
//                    JSONObject(a[i]).get("id"),
//
//                )
//                Log.d("dsfa", b.toString())
////                projectsList.add(a[i])
//            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecyclerViewMainAdapter(context!!)
        binding.recyclerviewMain.adapter = adapter
    }

}