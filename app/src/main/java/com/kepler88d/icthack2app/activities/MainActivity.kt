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
import android.view.animation.AccelerateInterpolator
import com.google.android.material.transition.platform.MaterialFadeThrough


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userData: User
    private val projectVacancyList = mutableListOf<String>()

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
            performTransformAnimation(binding.fabAddProject, binding.addProjectScreen.root)
        }

        binding.addProjectScreen.addMemberButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Выберите вакансию для добавления")
                .setItems(GlobalDataStorage.itTreeMap.keys.toTypedArray()) { dialog, which ->
                    run {
                        val selectedKey = GlobalDataStorage.itTreeMap.keys.toList()[which]
                        AlertDialog.Builder(this)
                            .setTitle(selectedKey)
                            .setItems(GlobalDataStorage.itTreeMap[selectedKey]!!.toTypedArray()) { dialog, which1 ->
                                run {
                                    val newVacancy =
                                        LayoutInflater.from(this).inflate(
                                            R.layout.item_vacancy,
                                            null,
                                            false
                                        )
                                    projectVacancyList.add(GlobalDataStorage.itTreeMap[selectedKey]!![which1])
                                    newVacancy.findViewWithTag<TextView>("text").text =
                                        GlobalDataStorage.itTreeMap[selectedKey]!![which1]

                                    newVacancy.findViewWithTag<CardView>("clickableCard")
                                        .setOnClickListener {
                                            projectVacancyList.remove(GlobalDataStorage.itTreeMap[selectedKey]!![which1])
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
            val dialog = AlertDialog.Builder(this)
                .setMessage("Подтвердить создание проекта ${binding.addProjectScreen.descriptionInputField.editText!!.text}?")
                .setNegativeButton("Отмена") { dialog, which -> run {} }
                .setPositiveButton("Да, подтвердить") { dialog, which ->
                    run {
                        RequestWorker.addProject(
                            name = binding.addProjectScreen.projectNameInputField.editText!!.text.toString(),
                            description = binding.addProjectScreen.projectNameInputField.editText!!.text.toString(),
                            githubProjectLink = binding.addProjectScreen.githubRepoLinkInputField.editText!!.text.toString(),
                            ownerId = userData.id
                        )
                        performTransformAnimation(
                            binding.addProjectScreen.root,
                            binding.fabAddProject
                        )
                    }
                }.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
        }

        binding.addProjectScreen.buttonCloseAddProject.setOnClickListener {
            performTransformAnimation(binding.addProjectScreen.root, binding.fabAddProject)
        }

        addFabAnimation()
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottom.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        addChip("Android")
        addChip("IOS")
        addChip("Project management")
        addChip("Machine learning")

        OverScrollDecoratorHelper.setUpOverScroll(binding.addProjectScreen.addProjectCardView)
        binding.bottom.buttonClose.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bottom.textViewTelegram.setOnClickListener {
            val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${binding.bottom.textViewTelegram.text}"))
            telegram.setPackage("org.telegram.messenger")
            startActivity(telegram)
        }

        OverScrollDecoratorHelper.setUpOverScroll(binding.addProjectCardView)

        binding.loadSplashScreen.visibility = View.VISIBLE
        binding.fabAddProject.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
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
        }, 3000)
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

    private fun addChip(str: String) {
        val chip = Chip(this)
        chip.text = str
//        chip.setChipBackgroundColorResource(R.color.background_dark)
////        chip.isCloseIconVisible = true
//        chip.setTextColor(resources.getColor(R.color.white))
////        chip.setTextAppearance(R.style.ChipTextAppearance)
        binding.bottom.chipGroupSkills.addView(chip)
    }

}