package com.kepler88d.icthack2app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.databinding.ActivityMainBinding
import com.kepler88d.icthack2app.fragments.MainFragment
import com.kepler88d.icthack2app.fragments.NotificationsFragment
import com.kepler88d.icthack2app.model.Project
import com.kepler88d.icthack2app.model.Reply
import com.kepler88d.icthack2app.model.RequestWorker
import com.kepler88d.icthack2app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject


val api = RequestWorker()
class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewpagerMain.apply {
            adapter = pagerAdapter
        }

        CoroutineScope(Dispatchers.IO).launch {
            val a = api
            val str = a.getRepliesList()
//            Log.d("sadasd", str)
//            Log.d("sadasd", JSONObject(str).getString("name"))
//            val user = User(
//                id=JSONObject(str).getString("id"),
//                password=JSONObject(str).getString("password"),
//
//                )


//            Log.d("checkrequest", "JSON: ${b.description}")
//            Log.d("checkrequest", "JSON: ${a.getRepliesList()}")
//            Log.d("checkrequest", "JSON: ${a.getUsersList()}")
//            Log.d("checkrequest", "JSON: ${a.getProjectById(1)}")
//            Log.d("checkrequest", "JSON: ${a.getUserById(1)}")
//            Log.d("checkrequest", "JSON: ${a.getReplyById(1)}")
        }

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