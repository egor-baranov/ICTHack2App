package com.kepler88d.icthack2app.activities

import android.R
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.kepler88d.icthack2app.databinding.ActivityMainBinding
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.fragments.NotificationsFragment
import com.kepler88d.icthack2app.model.data.Project


class MainActivity : FragmentActivity() {
    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewpagerMain.apply {
            adapter = pagerAdapter
        }

        binding.fabAddProject.setOnClickListener {
            performTransformAnimation(binding.fabAddProject, binding.addProjectCardView)
        }

        binding.closeAddProjectButton.setOnClickListener {
            performTransformAnimation(binding.addProjectCardView, binding.fabAddProject)
        }

        binding.materialButton.setOnClickListener {
            performTransformAnimation(binding.addProjectCardView, binding.fabAddProject)
        }

        addFabAnimation()
        var bottomSheetBehavior = BottomSheetBehavior.from(binding.bottom.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        addChip("Android")
        addChip("IOS")
        addChip("Project managment")
        addChip("Machine learning")
    }

    fun fillProjectInfo(projectData: Project) {
        binding.projectScreen.textViewProjectName.text = projectData.name
        binding.projectScreen.textViewDescription.text = projectData.description
        binding.projectScreen.textViewRepo.text = projectData.githubProjectLink

    }

    private fun addFabAnimation() {
        binding.viewpagerMain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            var currentPage = binding.viewpagerMain.currentItem

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 1) hideFab(positionOffset) else showFab(positionOffset)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })
    }

    fun showFab(positionOffset: Float = 1F) =
        binding.fabAddProject.animate().scaleX(1 - positionOffset)
            .scaleY(1 - positionOffset).setDuration(0).start()


    fun hideFab(positionOffset: Float = 0F) =
        binding.fabAddProject.animate().scaleX(positionOffset).scaleY(positionOffset)
            .setDuration(0).start()


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            if (position == 0) MainFragment() else NotificationsFragment()
    }

    fun performTransformAnimation(firstView: View, secondView: View) {
        val transform = MaterialContainerTransform().apply {
            startView = firstView
            endView = secondView

            addTarget(secondView)

            pathMotion = MaterialArcMotion()
            scrimColor = Color.TRANSPARENT
        }

        TransitionManager.beginDelayedTransition(binding.root, transform)

        firstView.visibility = View.GONE
        secondView.visibility = View.VISIBLE
    }

    private fun addChip(str: String){
        val chip = Chip(this)
        chip.text = str
//        chip.setChipBackgroundColorResource(R.color.background_dark)
////        chip.isCloseIconVisible = true
//        chip.setTextColor(resources.getColor(R.color.white))
////        chip.setTextAppearance(R.style.ChipTextAppearance)
        binding.bottom.chipGroupSkills.addView(chip)
    }

}