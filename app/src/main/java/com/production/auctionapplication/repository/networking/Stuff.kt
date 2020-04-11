package com.production.auctionapplication.repository.networking

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * To get response data when request all stuff data.
 */
data class StuffJsonResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: List<Stuff>?
)

data class CreateUpdateStuffResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: Stuff?
)

@Parcelize
data class Stuff(
    @Json(name = "stuff_id") var stuffId: Int?,
    @Json(name = "category_id") var categoryId: Int?,
    @Json(name = "stuff_name") var stuffName: String?,
    @Json(name = "started_price") var startedPrice: Int?,
    var description: String?,
    @Json(name = "image_url") var imageUrl: String?,
    var status: String?,
    var date: String?
) : Parcelable {

    // to get state from the stuff are the stuff
    // is saved or auctioned off
    val isSaved get() = status == "disimpan"
}