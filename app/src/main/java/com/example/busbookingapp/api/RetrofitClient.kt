package com.example.busbookingapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://sandbox.safaricom.co.ke/"


    val mpesaService: MpesaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpesaApi::class.java)
    }
}