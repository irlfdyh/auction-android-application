package com.production.auctionapplication.repository.networking

import com.production.auctionapplication.repository.database.OfficerAuth
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuctionApiService {

    /**
     * Authentication
     */
    @FormUrlEncoded
    @POST("auth/signin")
    fun officerSignInAsync(
        @Field("username") username: String,
        @Field("password") password: String
    ): Deferred<OfficerAuth>

    /**
     * Stuff section
     */
    @GET("v1/stuff")
    fun getAllStuffAsync(): Deferred<StuffJsonResponse>

    /**
     * Category section
     */
    @GET("v1/category")
    fun getAllCategoryAsync(): Deferred<CategoryJsonResponse>

    /**
     * Officer section
     */
    @GET("v1/officer")
    fun getAllOfficerAsync(): Deferred<OfficerJsonResponse>
}