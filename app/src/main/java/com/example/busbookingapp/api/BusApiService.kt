package com.example.busbookingapp.api

import com.example.busbookingapp.analytics.AdminStatsResponse // We will define this next
import retrofit2.http.GET

interface BusApiService {
    // This tells Retrofit to fetch data from the "admin/stats" endpoint
    @GET("admin/stats")
    suspend fun getAdminStats(): AdminStatsResponse
}

