package com.production.auctionapplication.repository.networking.models.officer

import com.squareup.moshi.Json

/**
 * Model class to get response when request all officer data
 */
data class RequestAllOfficerResponse(
    @Json(name = "message") var message: String,
    @Json(name = "officier") var officer: List<OfficerResponse>
)

/**
 * Model class to get response when create or update
 * data.
 */
data class CreateUpdateOfficerResponse(
    @Json(name = "message") val message: String,
    @Json(name = "user") val officerData: OfficerData
)

data class OfficerResponse(
    @Json(name = "officer_id") var officerId: Int,
    @Json(name = "user_id") var userId: Int,
    @Json(name = "officer_name") var officerName: String,
    @Json(name = "image_url") var imageUrl: String?,
    @Json(name = "status") var status: String
)

data class OfficerData (
    @Json(name = "officer_name") val officerName: String,
    @Json(name = "level_id") val levelId: String,
    @Json(name = "token") val token: String
)