package com.production.auctionapplication.networking

import com.squareup.moshi.Json

data class StuffJsonResponse(
    @Json(name = "message") var message: String,
    @Json(name = "stuff") var stuffData: List<Stuff>
)

data class Stuff(
    @Json(name = "stuff_id") var stuffId: Int,
    @Json(name = "category_id") var categoryId: Int,
    @Json(name = "stuff_name") var stuffName: String,
    @Json(name = "started_price") var startedPrice: Int,
    var description: String,
    var status: String,
    var date: String
)