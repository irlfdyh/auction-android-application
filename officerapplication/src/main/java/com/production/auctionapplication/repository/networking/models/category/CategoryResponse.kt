package com.production.auctionapplication.repository.networking.models.category

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class RequestAllCategoryResponse(
    @Json(name = "message") var message: String,
    @Json(name = "category") var categoryData: List<CategoryResponse>
)

data class CreateUpdateCategoryResponse(
    @Json(name = "message") var message: String,
    @Json(name = "category") var categoryData: CategoryResponse
)

@Parcelize
data class CategoryResponse(
    @Json(name = "category_id") var categoryId: Int,
    @Json(name = "category_name") var categoryName: String,
    @Json(name = "category_description") var categoryDescription: String,
    @Json(name = "image_url") var imageUrl: String
) : Parcelable


/**
 * This function is used to only get name from all
 * field at category list data.
 */
fun RequestAllCategoryResponse.getCategoryName(): List<String> {
    return categoryData.map {
        it.categoryName
    }
}