package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.adapters.RecyclerViewMainAdapter
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.Project
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MainFragment : Fragment(R.layout.fragment_main) {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: RecyclerViewMainAdapter
    val recyclerList = mutableListOf<Project>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        binding.searchField.setOnClickListener {
            (requireActivity() as MainActivity).performTransformAnimation(
                binding.searchField,
                binding.searchBox
            )
        }

        binding.applySearchButton.setOnClickListener {
            (requireActivity() as MainActivity).performTransformAnimation(
                binding.searchBox,
                binding.searchField
            )
        }

        binding.closeSearchButton.setOnClickListener {
            (requireActivity() as MainActivity).performTransformAnimation(
                binding.searchBox,
                binding.searchField
            )
        }

        OverScrollDecoratorHelper.setUpOverScroll(
            (requireActivity() as MainActivity).binding.projectScreen.nestedScrollView
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            (requireActivity() as MainActivity).binding.profileScreen.rootScrollView
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.mainFragmentScrollView
        )

        binding.buttonUserAccount.setOnClickListener {
            (requireActivity() as MainActivity).performPageTransition(
                binding.fragmentMainRoot,
                (requireActivity() as MainActivity).binding.profileScreen.root
            )
        }

        (requireActivity() as MainActivity).binding.profileScreen.backFromProfilePageButton.setOnClickListener {
            (requireActivity() as MainActivity).performPageTransition(
                (requireActivity() as MainActivity).binding.profileScreen.root,
                binding.fragmentMainRoot
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerViewMainAdapter(requireActivity() as MainActivity, recyclerList)

        binding.recyclerviewMain.adapter = adapter
        val pager = (activity as MainActivity).binding.viewpagerMain
        binding.buttonNotifications.setOnClickListener {
            pager.setCurrentItem(2, true)
        }

        RequestWorker.getProjectList({ projectList: List<Project> ->
            requireActivity().runOnUiThread {
                recyclerList.addAll(projectList)
                adapter.notifyDataSetChanged()
                Log.d("ProjectList", projectList.toString())
            }
        })
    }
}