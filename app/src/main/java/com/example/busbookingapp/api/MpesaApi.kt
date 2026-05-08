package com.example.busbookingapp.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MpesaApi {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    suspend fun generateToken(
        @Header("Authorization") auth: String
    ): Response<TokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun initiateStkPush(
        @Header("Authorization") bearerToken: String,
        @Body request: StkPushRequest
    ): Response<StkPushResponse>
}

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: String
)

data class StkPushResponse(
    @SerializedName("MerchantRequestID")
    val merchantRequestId: String,
    @SerializedName("CheckoutRequestID")
    val checkoutRequestId: String,
    @SerializedName("ResponseCode")
    val responseCode: String,
    @SerializedName("ResponseDescription")
    val responseDescription: String,
    @SerializedName("CustomerMessage")
    val customerMessage: String
)