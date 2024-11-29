package com.c242ps413.clozify.data.retrofit

import com.c242ps413.clozify.data.api.response.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appId: String
    ): Response
}
