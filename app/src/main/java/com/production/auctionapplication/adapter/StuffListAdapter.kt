package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.StuffRowItemBinding
import com.production.auctionapplication.repository.networking.Stuff

class StuffListAdapter(private val clickListener: StuffListener) : ListAdapter<Stuff,
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
        fun bind(stuff: Stuff?, clickListener: StuffListener) {
            binding.stuff = stuff
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}

class StuffDiffCallback : DiffUtil.ItemCallback<Stuff>() {
    override fun areItemsTheSame(oldItem: Stuff, newItem: Stuff): Boolean {
        return oldItem.stuffId == newItem.stuffId
    }

    override fun areContentsTheSame(oldItem: Stuff, newItem: Stuff): Boolean {
        return oldItem == newItem
    }
}

class StuffListener(val clickListener: (stuffId: Int?) -> Unit) {
    fun onCLick(stuff: Stuff) = clickListener(stuff.stuffId)
}