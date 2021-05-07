package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.adapters.RecyclerViewRepliesAdapter
import com.kepler88d.icthack2app.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    lateinit var binding: FragmentNotificationsBinding
    lateinit var adapter: RecyclerViewRepliesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerViewRepliesAdapter(context!!)
        binding.recyclerviewNotifications.adapter = adapter
    }


}