package com.kepler88d.icthack2app.activities

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.kepler88d.icthack2app.databinding.ActivityMainBinding
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.fragments.NotificationsFragment

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

        binding.materialButton.setOnClickListener {
            performTransformAnimation(binding.addProjectCardView, binding.fabAddProject)
        }


    }

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
}