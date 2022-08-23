package com.ruviapps.newsapp.retrofit

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://newsapi.org"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface NewsApiService {
    @GET("/v2/everything")
    fun getNews(@Query("q") type: String,@Query("apiKey")apikey : String):
    Call<String>
}
object NewsApi{
    val retrofitService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}