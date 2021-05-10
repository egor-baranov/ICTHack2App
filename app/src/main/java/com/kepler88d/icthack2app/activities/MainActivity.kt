package com.kepler88d.icthack2app.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.databinding.ActivityMainBinding
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.fragments.NotificationsFragment
import com.kepler88d.icthack2app.model.GlobalDataStorage
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.data.Project
import com.kepler88d.icthack2app.model.data.User
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialFadeThrough

class MainActivity : AppCompatActivity() {

    enum class UiContext {
        OPENED_NOTHING,
        OPENED_ADDING_PROJECT,
        OPENED_PROJECT_SCREEN,
        OPENED_SEARCH_BAR,
        OPENED_BOTTOM_SHEET,
        OPENED_PROFILE,
        OPENED_NOTIFICATIONS
    }

    var uiContext: UiContext = UiContext.OPENED_NOTHING

    lateinit var binding: ActivityMainBinding
    lateinit var userData: User
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val projectVacancyList = mutableListOf<String>()

    fun TextInputLayout.text(): String = this.editText!!.text.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openFileInput("userData").use {
            userData = User.fromJsonString(it.readBytes().toString(Charsets.UTF_8))
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewpagerMain.apply {
            adapter = pagerAdapter
        }

        binding.fabAddProject.setOnClickListener {
            uiContext = UiContext.OPENED_ADDING_PROJECT
            performTransformAnimation(binding.fabAddProject, binding.addProjectScreen.root)
        }

        binding.addProjectScreen.addMemberButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Выберите вакансию для добавления")
                .setItems(GlobalDataStorage.itTreeMap.keys.toTypedArray()) { _, which ->
                    run {
                        val selectedKey = GlobalDataStorage.itTreeMap.keys.toList()[which]
                        AlertDialog.Builder(this)
                            .setTitle(selectedKey)
                            .setItems(GlobalDataStorage.itTreeMap[selectedKey]!!.toTypedArray()) { _, which1 ->
                                run {
                                    val newVacancy =
                                        LayoutInflater.from(this).inflate(
                                            R.layout.item_vacancy,
                                            null,
                                            false
                                        )
                                    projectVacancyList.add(
                                        GlobalDataStorage.itTreeMap[selectedKey]!![which1]
                                    )
                                    newVacancy.findViewWithTag<TextView>("text").text =
                                        GlobalDataStorage.itTreeMap[selectedKey]!![which1]

                                    newVacancy.findViewWithTag<CardView>("clickableCard")
                                        .setOnClickListener {
                                            projectVacancyList
                                                .remove(
                                                    GlobalDataStorage
                                                        .itTreeMap[selectedKey]!![which1]
                                                )
                                            binding.addProjectScreen.vacancyHolderLayout.removeView(
                                                newVacancy
                                            )
                                        }
                                    binding.addProjectScreen.vacancyHolderLayout.addView(newVacancy)
                                }
                            }
                            .create().show()
                    }
                }
                .create().show()
        }


        binding.addProjectScreen.createProjectButton.setOnClickListener {

            binding.addProjectScreen.projectNameInputField.error =
                if (binding.addProjectScreen.projectNameInputField.text().isEmpty())
                    "Добавьте название проекта" else null
            binding.addProjectScreen.descriptionInputField.error =
                if (binding.addProjectScreen.descriptionInputField.text().isEmpty())
                    "Заполните описание проекта" else null
            binding.addProjectScreen.githubRepoLinkInputField.error =
                if (binding.addProjectScreen.githubRepoLinkInputField.text().isEmpty())
                    "Добавьте ссылку на GitHub-репозиторий проекта" else null

            if (projectVacancyList.isEmpty()) {
                val dialog = AlertDialog.Builder(this)
                    .setMessage("Добавьте хотя бы одну вакансию для создания проекта")
                    .setPositiveButton("Хорошо") { _, _ -> run {} }.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            }

            val dialog = AlertDialog.Builder(this)
                .setMessage(
                    "Подтвердить создание проекта ${
                        binding.addProjectScreen.projectNameInputField.text()
                    }?"
                )
                .setNegativeButton("Отмена") { _, _ -> run {} }
                .setPositiveButton("Да, подтвердить") { _, _ ->
                    run {
                        RequestWorker.addProject(
                            name = binding.addProjectScreen.projectNameInputField.text(),
                            description = binding.addProjectScreen.descriptionInputField.text(),
                            githubProjectLink = "https://github.com/" +
                                    binding
                                        .addProjectScreen
                                        .githubRepoLinkInputField
                                        .editText!!
                                        .text
                                        .toString(),
                            ownerId = userData.id,
                            projectVacancyList
                        )
                        performTransformAnimation(
                            binding.addProjectScreen.root,
                            binding.fabAddProject
                        )
                    }
                }.create()

            if (binding.addProjectScreen.projectNameInputField.error == null &&
                binding.addProjectScreen.descriptionInputField.error == null &&
                binding.addProjectScreen.githubRepoLinkInputField.error == null &&
                projectVacancyList.isNotEmpty()
            ) {
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
            }
        }

        binding.addProjectScreen.buttonCloseAddProject.setOnClickListener {
            uiContext = UiContext.OPENED_NOTHING
            performTransformAnimation(binding.addProjectScreen.root, binding.fabAddProject)
        }

        addFabAnimation()
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottom.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        addChips(listOf("Android", "IOS", "Project management", "Machine learning"))

        binding.bottom.textViewTelegram.setOnClickListener {
            val telegram = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/${binding.bottom.textViewTelegram.text}")
            )
            telegram.setPackage("org.telegram.messenger")
            startActivity(telegram)
        }
        binding.bottom.buttonClose.setOnClickListener {
            uiContext = UiContext.OPENED_NOTIFICATIONS
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        OverScrollDecoratorHelper.setUpOverScroll(binding.addProjectScreen.addProjectCardView)

        binding.loadSplashScreen.visibility = View.VISIBLE
        binding.fabAddProject.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.fabAddProject.visibility = View.VISIBLE
                binding.loadSplashScreen
                    .animate()
                    .alpha(0f)
                    .setDuration(500)
                    .setInterpolator(AccelerateInterpolator())
                    .start()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loadSplashScreen.visibility = View.GONE
                }, 1000)
            }, 3000
        )
    }

    override fun onBackPressed() {
        when (uiContext) {
            UiContext.OPENED_ADDING_PROJECT -> {
                uiContext = UiContext.OPENED_NOTHING
                performTransformAnimation(binding.addProjectScreen.root, binding.fabAddProject)
            }

            UiContext.OPENED_PROJECT_SCREEN,
            UiContext.OPENED_PROFILE, UiContext.OPENED_SEARCH_BAR ->
                (supportFragmentManager.findFragmentByTag("f0") as IOnBackPressed)
                    .onBackPressed()

            UiContext.OPENED_NOTIFICATIONS ->
                binding.viewpagerMain.setCurrentItem(0, true)

            UiContext.OPENED_BOTTOM_SHEET -> {
                uiContext = UiContext.OPENED_NOTIFICATIONS
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            else -> this.onBackPressed()
        }
    }

    fun fillProjectInfo(projectData: Project) {
        binding.projectScreen.textViewProjectName.text = projectData.name
        binding.projectScreen.textViewDescription.text = projectData.description
        binding.projectScreen.textViewRepo.text = projectData.githubProjectLink

        binding.projectScreen.projectVacancyLinearLayout.removeAllViews()
        for (e in projectData.vacancy) {
            for (i in 0 until projectData.freeVacancy[e.key]!!) {
                val newView = LayoutInflater.from(this).inflate(
                    R.layout.item_vacancy, null, false
                )
                newView.findViewWithTag<TextView>("text").text = e.key
                binding.projectScreen.projectVacancyLinearLayout.addView(newView)
            }
            for (i in 0 until projectData.vacancy[e.key]!! -
                    projectData.freeVacancy.getOrElse(e.key) { 0 }) {
                val newView =
                    LayoutInflater.from(this).inflate(
                        R.layout.item_vacancy_locked, null, false
                    )
                newView.findViewWithTag<TextView>("text").text = e.key
                binding.projectScreen.projectVacancyLinearLayout.addView(newView)
            }
        }

        binding.projectScreen.cardViewRepo.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(binding.projectScreen.textViewRepo.text.toString())
                )
            )
        }

        binding.projectScreen.cardViewContributor.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.bottom.buttonReject.visibility = View.INVISIBLE
            binding.bottom.buttonAccept.visibility = View.INVISIBLE
        }

        binding.projectScreen.textViewRepliesCount.text =
            "Количество откликов: ${projectData.replyIdList.size}"
        RequestWorker.getUserById(projectData.ownerId, ({ user: User ->
            runOnUiThread {
                binding.projectScreen.textViewContributor.text =
                    "Владелец: ${user.firstName} ${user.lastName}"
            }
        }))

        binding.projectScreen.textViewMembersCount.text =
            "Количество участников: ${projectData.vacancy.values.sum() + 1}"

        binding.projectScreen.textViewProjectName2.text =
            "Вакансии на проект (${projectData.freeVacancy.values.sum()} из ${projectData.vacancy.values.sum()})"
    }

    private fun addFabAnimation() {
        binding.viewpagerMain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                if (position == 1) {
                    hideFab(positionOffset)
                } else {
                    showFab(positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                uiContext = if (position == 1) {
                    UiContext.OPENED_NOTIFICATIONS
                } else {
                    UiContext.OPENED_NOTHING
                }
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

    private fun addChips(list: List<String>) =
        list.forEach {
            val chip = Chip(this)
            chip.text = it
            binding.bottom.chipGroupSkills.addView(chip)
        }
}


interface IOnBackPressed {
    fun onBackPressed()
}