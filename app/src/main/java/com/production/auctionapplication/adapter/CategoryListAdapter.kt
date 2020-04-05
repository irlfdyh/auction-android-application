package com.production.auctionapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.production.auctionapplication.databinding.StuffCategoryRowItemBinding
import com.production.auctionapplication.repository.networking.Category

class CategoryListAdapter(private val onClickListener: OnClickListener) : ListAdapter<Category,
        CategoryListAdapter.StuffCategoryViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffCategoryViewHolder {
        return StuffCategoryViewHolder(StuffCategoryRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: StuffCategoryViewHolder, position: Int) {
        val stuffCategory = getItem(position)
        holder.bind(stuffCategory)

        onClickListener.onClick(stuffCategory)
    }

    class StuffCategoryViewHolder(private var binding: StuffCategoryRowItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(stuffCategory: Category?) {
            binding.category = stuffCategory
            binding.executePendingBindings()

        }
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

/**
 * Row item click handler
 */
class OnClickListener (val clickListener: (stuffCategory: Category) -> Unit) {
    fun onClick(stuffCategory: Category) = clickListener(stuffCategory)
}
