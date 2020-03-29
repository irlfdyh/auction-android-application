package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.OfficerRowItemBinding
import com.production.auctionapplication.repository.networking.Category
import com.production.auctionapplication.repository.networking.Officer

class OfficerListAdapter : ListAdapter <Officer,
        OfficerListAdapter.OfficerViewHolder>(OfficerDiffCallback()){

    class OfficerViewHolder(private var binding: OfficerRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(officer: Officer?) {
            binding.officer = officer
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficerViewHolder {
        return OfficerViewHolder(OfficerRowItemBinding.inflate(
            LayoutInflater.from(parent.context)
        ))
    }

    override fun onBindViewHolder(holder: OfficerViewHolder, position: Int) {
        val officer = getItem(position)
        holder.bind(officer)
    }
}

class OfficerDiffCallback : DiffUtil.ItemCallback<Officer>() {
    override fun areItemsTheSame(oldItem: Officer, newItem: Officer): Boolean {
        return oldItem.officerId == newItem.officerId
    }

    override fun areContentsTheSame(oldItem: Officer, newItem: Officer): Boolean {
        return oldItem == newItem
    }
}

/**
 * Row item click handler
 */
class OnClick (val clickListener: (stuffCategory: Category) -> Unit) {
    fun onClick(stuffCategory: Category) = clickListener(stuffCategory)
}