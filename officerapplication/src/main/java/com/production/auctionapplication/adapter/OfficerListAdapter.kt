package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.OfficerRowItemBinding
import com.production.auctionapplication.repository.networking.models.officer.OfficerResponse

class OfficerListAdapter(private val clickListener: OfficerListener) : ListAdapter <OfficerResponse,
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

        fun bind(officer: OfficerResponse?, clickListener: OfficerListener) {
            binding.officer = officer
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}

class OfficerDiffCallback : DiffUtil.ItemCallback<OfficerResponse>() {
    override fun areItemsTheSame(oldItem: OfficerResponse, newItem: OfficerResponse): Boolean {
        return oldItem.officerId == newItem.officerId
    }

    override fun areContentsTheSame(oldItem: OfficerResponse, newItem: OfficerResponse): Boolean {
        return oldItem == newItem
    }
}

/**
 * Row item click handler
 */
class OfficerListener (val clickListener: (officerData: OfficerResponse?) -> Unit) {
    fun onClick(officer: OfficerResponse) = clickListener(officer)
}