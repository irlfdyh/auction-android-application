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

    @FormUrlEncoded
    @POST("v1/stuff")
    fun createNewStuffAsync(
        @Query("token") token: String,
        @Field("category_id") categoryId: Int,
        @Field("stuff_name") stuffName: String,
        @Field("started_price") startedPrice: String,
        @Field("description") description: String,
        @Field("date") date: String
    ): Deferred<CreateUpdateStuffResponse>

    /**
     * Category section
     */
    @GET("v1/category")
    fun getAllCategoryAsync(): Deferred<CategoryJsonResponse>

    // Url : http//192.168.100.9:8000/api/v1/category?token={random string}
    @FormUrlEncoded
    @POST("v1/category")
    fun createNewCategoryAsync(
        @Query("token") token: String,
        @Field("category_name") categoryName: String,
        @Field("category_description") categoryDescription: String
    ): Deferred<CategoryJsonResponse>

    /**
     * Getting all available officer data
     */
    @GET("v1/officer")
    fun getAllOfficerAsync(): Deferred<OfficerJsonResponse>

    /**
     * at the endpoint creation all of the user is placed at
     * auth/user. So this endpoint isn't at the officer/ route.
     */
    @FormUrlEncoded
    @POST("auth/user")
    fun createNewOfficerAsync(
        @Field("level_id") levelId: Int,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("officer_name") officerName: String,
        @Field("phone") phone: String,
        @Field("status") status: String
    ) : Deferred<CreateOfficerJsonResponse>
}