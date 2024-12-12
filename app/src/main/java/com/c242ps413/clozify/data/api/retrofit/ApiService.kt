package com.c242ps413.clozify.data.retrofit

import com.c242ps413.clozify.data.api.response.MoodResponse
import com.c242ps413.clozify.data.api.response.RecomResponse
import com.c242ps413.clozify.data.api.response.Response
import com.c242ps413.clozify.ui.UserData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appId: String
    ): Response

    @Multipart
    @POST("classify")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): MoodResponse

    @POST("recommendations/")
    fun getRecommendations(
        @Body request: UserData
    ): Call<RecomResponse>
}
