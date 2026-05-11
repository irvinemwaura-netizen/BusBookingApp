package com.example.busbookingapp.api
data class MpesaResponse(
    val MerchantRequestID: String,
    val CheckoutRequestID: String,
    val ResponseDescription: String,
    val ResponseCode: String,
    val CustomerMessage: String
)