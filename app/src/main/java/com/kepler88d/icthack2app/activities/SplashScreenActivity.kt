package com.kepler88d.icthack2app.activities

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.databinding.ActivitySplashScreenBinding
import com.kepler88d.icthack2app.databinding.ActivityStartBinding

class SplashScreenActivity : Activity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val userDataFile = applicationContext.getFileStreamPath("userData")

        val intent =
            if (userDataFile != null && userDataFile.exists()) {
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, StartActivity::class.java)
            }
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}