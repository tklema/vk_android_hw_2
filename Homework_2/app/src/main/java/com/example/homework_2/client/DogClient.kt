package com.example.homework_2.client

import com.example.homework_2.data.DogImage
import retrofit2.http.GET
import retrofit2.http.Query

interface DogClient {
    @GET("v1/images/search")
    suspend fun getDog(
        @Query("limit") limit: Int = 7
    ): List<DogImage>

    companion object {
        const val BASE_URL = "https://api.thedogapi.com/"
    }
}