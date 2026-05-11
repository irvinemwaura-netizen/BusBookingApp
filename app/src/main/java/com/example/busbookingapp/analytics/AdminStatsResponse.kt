package com.example.busbookingapp.analytics


data class AdminStatsResponse(
    val dailyRevenue: Double,
    val revenueGrowth: Double,
    val occupancyRate: Int,
    val activeBuses: Int
)