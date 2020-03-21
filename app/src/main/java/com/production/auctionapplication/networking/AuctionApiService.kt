package com.production.auctionapplication.networking

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface AuctionApiService {

    /**
     * Stuff section
     */
    @GET("stuff")
    fun getAllStuffAsync(): Deferred<StuffJsonResponse>

    /**
     * Category section
     */
    @GET("category")
    fun getAllCategoryAsync(): Deferred<CategoryJsonResponse>

    /**
     * Officer section
     */
    @GET("officer")
    fun getAllOfficerAsync(): Deferred<OfficerJsonResponse>
}