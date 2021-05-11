package com.kepler88d.icthack2app.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.IOnBackPressed
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.activities.SplashScreenActivity
import com.kepler88d.icthack2app.adapters.RecyclerViewMainAdapter
import com.kepler88d.icthack2app.databinding.FragmentMainBinding
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.Project
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MainFragment : Fragment(R.layout.fragment_main), IOnBackPressed {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: RecyclerViewMainAdapter
    private val recyclerList = mutableListOf<Project>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        binding.searchField.setOnClickListener {
            (requireActivity() as MainActivity).uiContext = MainActivity.UiContext.OPENED_SEARCH_BAR
            (requireActivity() as MainActivity).performTransformAnimation(
                binding.searchField,
                binding.searchBox
            )
        }

        binding.applySearchButton.setOnClickListener {
            performSearch(binding.searchEditText.text.toString())
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

        OverScrollDecoratorHelper.setUpOverScroll(
            binding.recyclerviewMain, OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )

        binding.buttonUserAccount.setOnClickListener {
            (requireActivity() as MainActivity).uiContext = MainActivity.UiContext.OPENED_PROFILE
            (requireActivity() as MainActivity).performPageTransition(
                binding.fragmentMainRoot,
                (requireActivity() as MainActivity).binding.profileScreen.root
            )
            fillProfileData()
        }

        (requireActivity() as MainActivity)
            .binding.profileScreen.backFromProfilePageButton.setOnClickListener {
                (requireActivity() as MainActivity).uiContext =
                    MainActivity.UiContext.OPENED_NOTHING
                performCloseProfileScreen()
            }

        return binding.root
    }

    private fun performCloseProfileScreen() {
        (requireActivity() as MainActivity).performPageTransition(
            (requireActivity() as MainActivity).binding.profileScreen.root,
            binding.fragmentMainRoot
        )
    }

    private fun performCloseSearchBar() {
        (requireActivity() as MainActivity).uiContext = MainActivity.UiContext.OPENED_NOTHING
        (requireActivity() as MainActivity).performTransformAnimation(
            binding.searchBox,
            binding.searchField
        )
    }

    private fun fillProfileData() {
        val userData = (requireActivity() as MainActivity).userData
        val activity = (requireActivity() as MainActivity)
        activity.binding.profileScreen.textViewName.text = userData.fullName()
        activity.binding.profileScreen.textView3.text = "Рейтинг ${userData.rating}"

        activity.binding.profileScreen.profileDescription.text = userData.profileDescription
        activity.binding.profileScreen.logoutButton.setOnClickListener {
            requireActivity().deleteFile("userData")

            val intent = Intent(context, SplashScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        RequestWorker.getProjectList(
            { projectList ->
                activity.runOnUiThread {
                    activity.binding.profileScreen.projectsText.text =
                        projectList.filter { it.ownerId == userData.id }
                            .joinToString(separator = ",\n") { it.name.trim() }
                }
            }
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
            }
        })
    }

    private fun performSearch(searchText: String) {
        Log.d("Search", searchText)
    }

    override fun onBackPressed() {
        when ((requireActivity() as MainActivity).uiContext) {
            MainActivity.UiContext.OPENED_SEARCH_BAR -> {
                (requireActivity() as MainActivity).uiContext =
                    MainActivity.UiContext.OPENED_NOTHING
                performCloseSearchBar()
            }
            MainActivity.UiContext.OPENED_PROJECT_SCREEN -> {
                (requireActivity() as MainActivity).uiContext =
                    MainActivity.UiContext.OPENED_NOTHING
                (adapter as IOnBackPressed2).onBackPressed()
            }
            MainActivity.UiContext.OPENED_PROFILE -> {
                (requireActivity() as MainActivity).uiContext =
                    MainActivity.UiContext.OPENED_NOTHING
                performCloseProfileScreen()
            }
            else -> {
            }
        }
    }
}

interface IOnBackPressed2 {
    fun onBackPressed()
}