package com.personal.animeshpandey.agrisecure.Data.openweathermap

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val myretrofitobject = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val airqualityservice = myretrofitobject.create(ApiService::class.java)

interface ApiService{
    @GET("air_pollution?lat=50&lon=50&appid=834f49643bfbfc708229b8317f91a7e1")
    suspend fun fetchAirQuality():Response
}