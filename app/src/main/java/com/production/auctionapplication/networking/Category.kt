package com.production.auctionapplication.networking

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class CategoryJsonResponse(
    @Json(name = "message") var message: String,
    @Json(name = "category") var categoryData: List<Category>
)

@Parcelize
data class Category(
    @Json(name = "category_id") var categoryId: Long,
    @Json(name = "category_name") var categoryName: String,
    @Json(name = "category_description") var categoryDescription: String
) : Parcelable