package com.production.auctionapplication.repository.networking.models.stuff

import android.os.Parcelable
import com.production.auctionapplication.repository.networking.models.category.CategoryResponse
import com.production.auctionapplication.repository.networking.models.officer.OfficerResponse
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * To get response data when request all stuff data.
 */
data class RequestAllStuffResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: List<StuffResponse?>
)

data class CreateUpdateStuffResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: StuffResponse?
)

data class RequestDetailStuffResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: StuffDetailResponse?
)

@Parcelize
data class StuffResponse(
    @Json(name = "stuff_id") var stuffId: Int?,
    @Json(name = "category_id") var categoryId: Int?,
    @Json(name = "officer_id") var officerId: Int?,
    @Json(name = "stuff_name") var stuffName: String?,
    @Json(name = "started_price") var startedPrice: Int?,
    @Json(name = "description") var description: String?,
    @Json(name = "image_url") var imageUrl: String?,
    @Json(name = "status") var status: String?,
    @Json(name = "date") var date: String?
) : Parcelable {

    // to get state from the stuff are the stuff
    // is saved or auctioned off
    val isSaved get() = status == "disimpan"
}

data class StuffDetailResponse(
    @Json(name = "stuff_id") var stuffId: Int?,
    @Json(name = "category_id") var categoryId: Int?,
    @Json(name = "officer_id") var officerId: Int?,
    @Json(name = "stuff_name") var stuffName: String?,
    @Json(name = "started_price") var startedPrice: Int?,
    @Json(name = "description") var description: String?,
    @Json(name = "image_url") var imageUrl: String?,
    @Json(name = "status") var status: String?,
    @Json(name = "date") var date: String?,
    @Json(name = "category") var stuffCategory: CategoryResponse?,
    @Json(name = "officer") var officer: OfficerResponse?
)