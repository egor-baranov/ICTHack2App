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

enum class StartActivityScreen {
    DOES_ACCOUNT_EXIST,
    FIRST_SCREEN,
    SECOND_SCREEN,
    LOGIN_SCREEN
}

class StartActivity : Activity() {
    lateinit var binding: ActivityStartBinding
    var currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            performPageTransition(binding.isUserLoginedScreen, binding.firstScreen)
            currentScreen = StartActivityScreen.FIRST_SCREEN
        }

        binding.loginButon.setOnClickListener {
            performPageTransition(binding.isUserLoginedScreen, binding.loginScreen)
            currentScreen = StartActivityScreen.LOGIN_SCREEN
        }

        binding.materialButton.setOnClickListener {
            performPageTransition(binding.firstScreen, binding.secondScreen)
            currentScreen = StartActivityScreen.SECOND_SCREEN
        }

        binding.backFromLoginButton.setOnClickListener {
            performPageTransition(binding.loginScreen, binding.isUserLoginedScreen)
            currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST
        }

        binding.backToChooseButton.setOnClickListener {
            performPageTransition(binding.firstScreen, binding.isUserLoginedScreen)
            currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST
        }

        binding.backButton.setOnClickListener {
            performPageTransition(binding.secondScreen, binding.firstScreen)
            currentScreen = StartActivityScreen.FIRST_SCREEN
        }

        binding.materialButton2.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.authorizeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.specializationLayout.setOnClickListener {
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

    override fun onBackPressed() {
        when (currentScreen) {
            StartActivityScreen.DOES_ACCOUNT_EXIST -> super.onBackPressed()
            StartActivityScreen.FIRST_SCREEN -> {
                performPageTransition(binding.firstScreen, binding.isUserLoginedScreen)
                currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST
            }
            StartActivityScreen.SECOND_SCREEN -> {
                performPageTransition(binding.secondScreen, binding.firstScreen)
                currentScreen = StartActivityScreen.FIRST_SCREEN
            }
            StartActivityScreen.LOGIN_SCREEN -> {
                performPageTransition(binding.loginScreen, binding.isUserLoginedScreen)
                currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST
            }
        }

    }
}