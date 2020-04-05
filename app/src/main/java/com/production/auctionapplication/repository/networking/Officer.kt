package com.production.auctionapplication.repository.networking

import com.squareup.moshi.Json

/**
 * Model class to get response when request all officer data
 */
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

/**
 * Model class to hold officer data
 */
data class CreateUpdateOfficer (
    var levelId: String,
    var username: String,
    var password: String,
    var officerName: String,
    var phone: String,
    var status: String
)

/**
 * Model class to get response when create or update
 * data.
 */
data class CreateOfficerJsonResponse(
    @Json(name = "message") val message: String,
    @Json(name = "user") val officerData: OfficerData
)

data class OfficerData (
    @Json(name = "officer_name")
    val responseName: String,
    @Json(name = "level_id")
    val levelId: String,
    @Json(name = "token")
    val token: String
)