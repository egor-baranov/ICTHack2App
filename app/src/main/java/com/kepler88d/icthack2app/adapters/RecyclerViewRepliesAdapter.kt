package com.kepler88d.icthack2app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kepler88d.icthack2app.databinding.ItemReplyBinding

class RecyclerViewRepliesAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ReplyItem(val binding: ItemReplyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.buttonDelete.setOnClickListener {
                notifyItemRemoved(position)
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