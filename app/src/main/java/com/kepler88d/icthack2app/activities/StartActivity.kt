package com.kepler88d.icthack2app.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.kepler88d.icthack2app.adapters.RecyclerViewCheckboxAdapter
import com.kepler88d.icthack2app.databinding.ActivityStartBinding
import com.kepler88d.icthack2app.model.GlobalDataStorage
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.User
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

enum class StartActivityScreen {
    DOES_ACCOUNT_EXIST,
    FIRST_SCREEN,
    SECOND_SCREEN,
    LOGIN_SCREEN
}

class StartActivity : Activity() {
    private lateinit var binding: ActivityStartBinding
    private var currentScreen = StartActivityScreen.DOES_ACCOUNT_EXIST
    lateinit var adapter: RecyclerViewCheckboxAdapter
    val listSkills = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addEdittextChangeListener()

        binding.registerButton.setOnClickListener {
            performPageTransition(binding.isUserLoginedScreen, binding.firstScreen)
            currentScreen = StartActivityScreen.FIRST_SCREEN
        }

        binding.loginButon.setOnClickListener {
            performPageTransition(binding.isUserLoginedScreen, binding.loginScreen)
            currentScreen = StartActivityScreen.LOGIN_SCREEN
        }

        binding.buttonAcceptSpecializes.setOnClickListener {
            performTransformAnimation(binding.specializationList, binding.card)
            binding.chipgrougStart.removeAllViews()
            adapter.list.forEach {
                if(it.checked){
                    listSkills.add(it.name)
                    val chip = Chip(this)
                    chip.text = it.name
                    binding.chipgrougStart.addView(chip)
                }
            }
        }

        binding.materialButton.setOnClickListener {
            if (binding.firstNameTextField.editText!!.text.isEmpty() ||
                binding.lastNameTextField.editText!!.text.isEmpty() ||
                binding.isuIdTextField.editText!!.text.isEmpty() ||
                binding.passwordIdTextField.editText!!.text.isEmpty()
            ) {
                binding.firstNameTextField.error =
                    if (binding.firstNameTextField.editText!!.text.isEmpty()) "Заполните поле" else null
                binding.lastNameTextField.error =
                    if (binding.lastNameTextField.editText!!.text.isEmpty()) "Заполните поле" else null
                binding.isuIdTextField.error =
                    if (binding.isuIdTextField.editText!!.text.isEmpty()) "Заполните поле" else null
                binding.passwordIdTextField.error =
                    if (binding.passwordIdTextField.editText!!.text.isEmpty()) "Заполните поле" else null
            } else {
                binding.firstNameTextField.error = null
                binding.lastNameTextField.error = null
                binding.isuIdTextField.error = null
                binding.passwordIdTextField.error = null

                performPageTransition(binding.firstScreen, binding.secondScreen)
                currentScreen = StartActivityScreen.SECOND_SCREEN
            }
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

        adapter =  RecyclerViewCheckboxAdapter(GlobalDataStorage.itTreeMap)
        binding.recyclerViewCheckboxes.adapter = adapter

        binding.loadSplashScreen.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.loadSplashScreen
                .animate()
                .alpha(0f)
                .setDuration(500)
                .setInterpolator(AccelerateInterpolator())
                .start()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loadSplashScreen.visibility = View.GONE
            }, 1000)
        }, 3000)
    }

    private fun addEdittextChangeListener() {
        binding.firstNameTextField.editText?.addTextChangedListener {
            binding.firstNameTextField.error = null
        }
        binding.lastNameTextField.editText?.addTextChangedListener {
            binding.lastNameTextField.error = null
        }
        binding.isuIdTextField.editText?.addTextChangedListener {
            binding.isuIdTextField.error = null
        }
        binding.passwordIdTextField.editText?.addTextChangedListener {
            binding.passwordIdTextField.error = null
        }
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
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
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
            specializationList = listOf(),
            githubProfileLink = binding.githubInputField.editText!!.text.toString(),
            handler = { user: User ->
                run {
                    this.openFileOutput("userData", Context.MODE_PRIVATE).write(
                        user.toJson().toString().toByteArray()
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
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

    fun performPageTransition(firstPage: View, secondPage: View) {
        val fadeThrough = MaterialFadeThrough()

        TransitionManager.beginDelayedTransition(binding.root, fadeThrough)

        firstPage.visibility = View.GONE
        secondPage.visibility = View.VISIBLE
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