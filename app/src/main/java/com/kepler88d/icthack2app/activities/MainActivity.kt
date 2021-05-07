package com.kepler88d.icthack2app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.databinding.ActivityMainBinding
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.fragments.NotificationsFragment

class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewpagerMain.adapter = pagerAdapter
    }



    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int) : Fragment {
            when(position)
            {
                0 -> return MainFragment()
                1 -> return NotificationsFragment()
            }
            return MainFragment()
        }
    }



}