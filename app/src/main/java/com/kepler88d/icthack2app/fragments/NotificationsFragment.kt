package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.adapters.RecyclerViewRepliesAdapter
import com.kepler88d.icthack2app.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    lateinit var binding: FragmentNotificationsBinding
    lateinit var adapter: RecyclerViewRepliesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)

//        for (i in 1..10) {
//            addNotificationView("Notification №$i")
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerViewRepliesAdapter(context!!)
        binding.recyclerviewNotifications.adapter = adapter
    }

    private fun addNotificationView(headerText: String) {
        val newNotificationView: View = LayoutInflater.from(requireContext().applicationContext)
            .inflate(R.layout.item_reply, null, false)
        newNotificationView.findViewWithTag<TextView>("header").text = headerText
    }
}