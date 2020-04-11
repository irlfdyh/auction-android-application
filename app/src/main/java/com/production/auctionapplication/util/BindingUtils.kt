package com.production.auctionapplication.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.production.auctionapplication.R
import com.production.auctionapplication.adapter.CategoryListAdapter
import com.production.auctionapplication.adapter.OfficerListAdapter
import com.production.auctionapplication.adapter.StuffListAdapter
import com.production.auctionapplication.repository.networking.Category
import com.production.auctionapplication.repository.networking.Officer
import com.production.auctionapplication.repository.networking.Stuff
import java.text.NumberFormat
import java.util.*

@BindingAdapter("category_list_data")
fun bindCategoryRecyclerView(recyclerView: RecyclerView, data: List<Category>?) {
    val adapter = recyclerView.adapter as CategoryListAdapter
    adapter.submitList(data)

    // Setting recyclerView layout manager
    val mLayoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = mLayoutManager
}

@BindingAdapter("officer_list_data")
fun bindOfficerRecyclerView(recyclerView: RecyclerView, data: List<Officer>?) {
    val adapter = recyclerView.adapter as OfficerListAdapter
    adapter.submitList(data)

    // Setting recyclerView layout manager
    val mLayoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = mLayoutManager

//    // Adding divider to every item
//    val mDividerItemDecoration =
//        DividerItemDecoration(recyclerView.context, mLayoutManager.orientation)
//    recyclerView.addItemDecoration(mDividerItemDecoration)
}

@BindingAdapter("stuff_list_data")
fun bindStuffRecyclerView(recyclerView: RecyclerView, data: List<Stuff>?) {
    val adapter = recyclerView.adapter as StuffListAdapter
    adapter.submitList(data)

    val mLayoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = mLayoutManager
}

@BindingAdapter("image_value")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .apply(RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image))
        .into(imageView)
}

@BindingAdapter("price_text")
fun bindPriceText(textView: TextView, priceInt: Int) {
    val localeID = Locale("in", "ID")
    val rupiahFormat = NumberFormat.getCurrencyInstance(localeID)

    textView.text = rupiahFormat.format(priceInt.toDouble())
}