package com.kepler88d.icthack2app.activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.kepler88d.icthack2app.adapters.RecyclerViewCheckboxAdapter
import com.kepler88d.icthack2app.databinding.ActivityStartBinding
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.User
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

enum class StartActivityScreen {
    DOES_ACCOUNT_EXIST,
    FIRST_SCREEN,
    SECOND_SCREEN,
    LOGIN_SCREEN
}

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private var currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST

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
            register()
        }

        binding.authorizeButton.setOnClickListener {
            login()
        }

        binding.specializationLayout.setOnClickListener {
            performTransformAnimation(binding.card, binding.specializationList)
        }

        binding.specializationList.setOnClickListener {
            performTransformAnimation(binding.specializationList, binding.card)
        }

        OverScrollDecoratorHelper.setUpOverScroll(
            binding.isUserLoginedScreen
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.secondScreen
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.firstScreen
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.specListScrollView
        )
        OverScrollDecoratorHelper.setUpOverScroll(
            binding.recyclerViewCheckboxes, OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )

        val map = mapOf(
            "Frontend-разработка" to listOf("React", "Vue", "Angular", "Flutter for web"),
            "Backend-разработка" to listOf("Flask", "Ktor", "Spring", "Django"),
            "Мобильная разработка" to listOf("Android", "IOS", "Flutter", "React Native"),
            "Data Science" to listOf(),
            "Computer Science" to listOf(),
            "Game Development" to listOf(),
            "Языки программирования" to listOf(
                "Python",
                "Kotlin",
                "Java",
                "C#",
                "C++",
                "Clojure",
                "Haskell"
            )
        )

        binding.recyclerViewCheckboxes.adapter = RecyclerViewCheckboxAdapter(this, map)
    }

    private fun login() {
        RequestWorker.authorizeUser(
            id = binding.loginIdtextField.editText!!.text.toString().toInt(),
            password = binding.loginPasswordIdTextField.editText!!.text.toString(),
            handler = { user: User ->
                run {
                    this.openFileOutput("userData", Context.MODE_PRIVATE)
                        .write(
                            user.toJson().toString().toByteArray()
                        )
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                }
            },
            errorHandler = {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Что-то пошло не так, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun register() {
        RequestWorker.registerUser(
            id = binding.isuIdTextField.editText!!.text.toString().toInt(),
            firstName = binding.firstNameTextField.editText!!.text.toString(),
            lastName = binding.lastNameTextField.editText!!.text.toString(),
            password = binding.passwordIdTextField.editText!!.text.toString(),
            profileDescription = binding.descriptionInputField.editText!!.text.toString(),
            specialization = "",
            githubProfileLink = binding.githubInputField.editText!!.text.toString(),
            handler = { user: User ->
                run {
                    this.openFileOutput("userData", Context.MODE_PRIVATE).write(
                        user.toJson().toString().toByteArray()
                    )
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                }
            },
            errorHandler = {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "Что-то пошло не так, попробуйте еще раз",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
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