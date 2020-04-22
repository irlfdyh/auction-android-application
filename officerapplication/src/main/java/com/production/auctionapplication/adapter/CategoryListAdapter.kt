package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.StuffCategoryRowItemBinding
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse

class CategoryListAdapter(private val clickListener: StuffCategoryListener) : ListAdapter<CategoryResponse,
        CategoryListAdapter.StuffCategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffCategoryViewHolder {
        return StuffCategoryViewHolder(StuffCategoryRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: StuffCategoryViewHolder, position: Int) {
        val stuffCategory = getItem(position)
        holder.bind(stuffCategory, clickListener)
    }

    class StuffCategoryViewHolder(private var binding: StuffCategoryRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            stuffCategory: CategoryResponse?,
            clickListener: StuffCategoryListener
        ) {
            binding.category = stuffCategory
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryResponse>() {
    override fun areItemsTheSame(oldItem: CategoryResponse, newItem: CategoryResponse): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: CategoryResponse, newItem: CategoryResponse): Boolean {
        return oldItem == newItem
    }
}

/**
 * Row item click handler
 */
class StuffCategoryListener (val clickListener: (stuffCategory: Int) -> Unit) {
    fun onClick(stuffCategory: CategoryResponse) = clickListener(stuffCategory.categoryId!!)
}
