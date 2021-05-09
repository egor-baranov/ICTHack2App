package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.IOnBackPressed
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.activities.currentOpened
import com.kepler88d.icthack2app.adapters.RecyclerViewMainAdapter
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.Project
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MainFragment : Fragment(R.layout.fragment_main), IOnBackPressed {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: RecyclerViewMainAdapter
    val recyclerList = mutableListOf<Project>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        binding.searchField.setOnClickListener {
            currentOpened = MainActivity.OPENED_SEARCH_BAR
            Log.d("debugCheckOpened", "c: $currentOpened")
            (requireActivity() as MainActivity).performTransformAnimation(
                binding.searchField,
                binding.searchBox
            )
        }

        binding.applySearchButton.setOnClickListener {
            performCloseSearchBar()
        }

        binding.closeSearchButton.setOnClickListener {
            performCloseSearchBar()
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
            currentOpened = MainActivity.OPENED_PROFILE
            Log.d("debugCheckOpened", "c: $currentOpened")
            (requireActivity() as MainActivity).performPageTransition(
                binding.fragmentMainRoot,
                (requireActivity() as MainActivity).binding.profileScreen.root
            )
        }

        (requireActivity() as MainActivity).binding.profileScreen.backFromProfilePageButton.setOnClickListener {
            currentOpened = MainActivity.OPENED_NOTHING
            Log.d("debugCheckOpened", "c: $currentOpened")
            performCloseProfileScreen()
        }

        return binding.root
    }

    fun performCloseProfileScreen() {
        (requireActivity() as MainActivity).performPageTransition(
            (requireActivity() as MainActivity).binding.profileScreen.root,
            binding.fragmentMainRoot
        )
    }

    fun performCloseSearchBar() {
        currentOpened = MainActivity.OPENED_NOTHING
        Log.d("debugCheckOpened", "c: $currentOpened")
        (requireActivity() as MainActivity).performTransformAnimation(
            binding.searchBox,
            binding.searchField
        )
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

    override fun onBackPressed() {
        if (currentOpened == MainActivity.OPENED_SEARCH_BAR){
            currentOpened = MainActivity.OPENED_NOTHING
            performCloseSearchBar()
        }
        else if(currentOpened == MainActivity.OPENED_PROJECT_SCREEN){
            currentOpened = MainActivity.OPENED_NOTHING
            (adapter as IOnBackPressed2).onBackPressed()
        }
        else if(currentOpened == MainActivity.OPENED_PROFILE){
            currentOpened = MainActivity.OPENED_NOTHING
            performCloseProfileScreen()
        }

    }

}

interface IOnBackPressed2 {
    fun onBackPressed()
}