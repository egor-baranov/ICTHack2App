package com.kepler88d.icthack2app.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.kepler88d.icthack2app.databinding.ActivityStartBinding

class StartActivity : Activity() {
    lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialButton.setOnClickListener {
            performPageTransition(binding.firstScreen, binding.secondScreen)
        }

        binding.backButton.setOnClickListener {
            performPageTransition(binding.secondScreen, binding.firstScreen)
        }

        binding.materialButton2.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.specializationLayout.setOnClickListener{
            performTransformAnimation(binding.card, binding.specializationList)
        }

        binding.specializationList.setOnClickListener {
            performTransformAnimation(binding.specializationList, binding.card)
        }
    }

    private fun performPageTransition(firstPage: View, secondPage: View) {
        val fadeThrough = MaterialFadeThrough()

        TransitionManager.beginDelayedTransition(binding.root, fadeThrough)

        firstPage.visibility = View.GONE
        secondPage.visibility = View.VISIBLE
    }

    private fun performTransformAnimation(firstView: View, secondView: View) {
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