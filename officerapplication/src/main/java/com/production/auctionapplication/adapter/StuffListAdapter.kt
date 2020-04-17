package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.StuffRowItemBinding
import com.production.auctionapplication.repository.networking.models.stuff.StuffResponse

class StuffListAdapter(private val clickListener: StuffListener) : ListAdapter<StuffResponse,
        StuffListAdapter.StuffViewHolder>(StuffDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffViewHolder {
        return StuffViewHolder(StuffRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: StuffViewHolder, position: Int) {
        val stuff = getItem(position)
        holder.bind(stuff, clickListener)
    }

    class StuffViewHolder(private var binding: StuffRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(stuff: StuffResponse?, clickListener: StuffListener) {
            binding.stuff = stuff
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}

class StuffDiffCallback : DiffUtil.ItemCallback<StuffResponse>() {
    override fun areItemsTheSame(oldItem: StuffResponse, newItem: StuffResponse): Boolean {
        return oldItem.stuffId == newItem.stuffId
    }

    override fun areContentsTheSame(oldItem: StuffResponse, newItem: StuffResponse): Boolean {
        return oldItem == newItem
    }
}

class StuffListener(val clickListener: (stuffId: Int?) -> Unit) {
    fun onClick(stuff: StuffResponse) = clickListener(stuff.stuffId)
}