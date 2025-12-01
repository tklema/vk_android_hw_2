package com.example.homework_2.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitDog {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DogClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val dogClient: DogClient by lazy {
        retrofit.create(DogClient::class.java)
    }
}