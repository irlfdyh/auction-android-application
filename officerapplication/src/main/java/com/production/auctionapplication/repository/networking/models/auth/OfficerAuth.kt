package com.production.auctionapplication.repository.networking.models.auth

import com.production.auctionapplication.repository.database.OfficerAccountData
import com.squareup.moshi.Json

data class OfficerAuth(
    @Json(name = "message") var message: String,
    @Json(name = "level_id") var levelId: Int?,
    @Json(name = "token") var token: String?,
    @Json(name = "user") var userData: UserData?
)

data class UserData(
    @Json(name = "officer_id") var officerId: Int?,
    @Json(name = "user_id") var userId: Int?,
    @Json(name = "officer_name") var officerName: String?,
    @Json(name = "status") var status: String?
)

/**
 * Data transfer object..
 */
fun OfficerAuth.asDatabaseModel(): OfficerAccountData {
    return OfficerAccountData(
        userId = userData?.userId,
        officerId = userData?.officerId,
        levelId = levelId,
        token = token,
        officerName = userData?.officerName,
        status = userData?.status
    )
}