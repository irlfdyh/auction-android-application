package com.production.auctionapplication.repository.networking

import com.production.auctionapplication.repository.database.OfficerAuth
import kotlinx.coroutines.Deferred
import retrofit2.http.*

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

    // Url : http//192.168.100.9:8000/api/v1/
    @FormUrlEncoded
    @POST("v1/category")
    fun createNewCategoryAsync(
        @Query("token") token: String,
        @Field("category_name") categoryName: String,
        @Field("category_description") categoryDescription: String
    ): Deferred<CategoryJsonResponse>

    /**
     * Officer section
     */
    @GET("v1/officer")
    fun getAllOfficerAsync(): Deferred<OfficerJsonResponse>
}