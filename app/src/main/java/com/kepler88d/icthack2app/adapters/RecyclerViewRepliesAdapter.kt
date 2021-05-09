package com.kepler88d.icthack2app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kepler88d.icthack2app.R
import com.kepler88d.icthack2app.activities.MainActivity
import com.kepler88d.icthack2app.databinding.ItemReplyBinding

class RecyclerViewRepliesAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ReplyItem(val binding: ItemReplyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
//            binding.buttonDelete.setOnClickListener {
//                notifyItemRemoved(position)
//            }
            binding.itemReply.setOnClickListener {
                val bottomSheetBehavior = BottomSheetBehavior.from((context as MainActivity).binding.bottom.bottomSheet)
                if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                else{
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }


            }
            when(position%4){
                1 -> {
                    binding.textviewProject.text = "ЗАПРОС"
                    binding.textviewProject.setTextColor(context.getColor(R.color.state_request))
                }
                2 -> {
                    binding.textviewProject.text = "В ОЖИДАНИИ"
                    binding.textviewProject.setTextColor(context.getColor(R.color.state_waiting))
                }
                3 -> {
                    binding.textviewProject.text = "ПРИНЯТО"
                    binding.textviewProject.setTextColor(context.getColor(R.color.state_accepted))
                }
                0 -> {
                    binding.textviewProject.text = "ОТКЛОНЕНО"
                    binding.textviewProject.setTextColor(context.getColor(R.color.state_rejected))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReplyBinding.inflate(inflater, parent, false)
        return ReplyItem(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReplyItem).bind(position)
    }

    override fun getItemCount() = 200

}