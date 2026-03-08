package com.example.overwatchapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OverwatchApiClient {
    private const val BASE_URL = "https://overfast-api.tekrop.fr/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: OverwatchApiService by lazy {
        retrofit.create(OverwatchApiService::class.java)
    }
}
