package com.kepler88d.icthack2app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.adapters.RecyclerViewMainAdapter
import com.kepler88d.icthack2app.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: RecyclerViewMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

//        binding.searchBox.setOnClickListener {
//            (requireActivity() as MainActivity).performTransformAnimation(
//                binding.searchBox,
//                (requireActivity() as MainActivity).binding.searchCardView
//            )
//        }

//        (requireActivity() as MainActivity).binding.searchCardView.setOnClickListener {
//            (requireActivity() as MainActivity).performTransformAnimation(
//                (requireActivity() as MainActivity).binding.searchCardView,
//                binding.searchBox
//            )
//        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RecyclerViewMainAdapter(context!!)
        binding.recyclerviewMain.adapter = adapter
        val pager = (activity as MainActivity).binding.viewpagerMain
        binding.buttonNotifications.setOnClickListener {
            pager.setCurrentItem(2, true)
        }

    }
}