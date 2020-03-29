package com.production.auctionapplication.repository.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "http://192.168.100.9:8000/api/"

/**
 * Create Moshi object
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Create Retrofit object
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * Initialize the retrofit service
 */
object AuctionApi {
    val retrofitService : AuctionApiService by lazy {
        retrofit.create(
            AuctionApiService::class.java)
    }
}