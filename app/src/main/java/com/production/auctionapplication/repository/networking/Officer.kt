package com.production.auctionapplication.repository.networking

import com.squareup.moshi.Json

data class OfficerJsonResponse(
    var message: String,
    @Json(name = "officier") var officer: List<Officer>
)

data class Officer(
    @Json(name = "officer_id") var officerId: Int,
    @Json(name = "user_id") var userId: Int,
    @Json(name = "officer_name") var officerName: String,
    var status: String
)