package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.OfficerRowItemBinding
import com.production.auctionapplication.repository.networking.Category
import com.production.auctionapplication.repository.networking.Officer

class OfficerListAdapter(private val clickListener: OfficerListener) : ListAdapter <Officer,
        OfficerListAdapter.OfficerViewHolder>(OfficerDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfficerViewHolder {
        return OfficerViewHolder(OfficerRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: OfficerViewHolder, position: Int) {
        val officer = getItem(position)
        holder.bind(officer, clickListener)
    }

    class OfficerViewHolder(private var binding: OfficerRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(officer: Officer?, clickListener: OfficerListener) {
            binding.officer = officer
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
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
class OfficerListener (val clickListener: (officerId: Int?) -> Unit) {
    fun onClick(officer: Officer) = clickListener(officer.officerId)
}