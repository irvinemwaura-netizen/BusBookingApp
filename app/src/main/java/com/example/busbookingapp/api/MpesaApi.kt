package com.example.busbookingapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MpesaApi {
    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun initiateStkPush(
        @Header("Authorization") bearerToken: String,
        @Body request: StkPushRequest
    ): Response<MpesaResponse>
}