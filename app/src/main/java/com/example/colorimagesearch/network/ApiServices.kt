package com.example.colorimagesearch.network

import com.example.colorimagesearch.model.Colors
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("colors?")
    fun getColors(@Query("keywords") keywords: String,
                  @Query("format") format: String,
                  @Query("numResults") numResults: String): Call<List<Colors>>
}