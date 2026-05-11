package com.example.busbookingapp.analytics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.example.busbookingapp.api.BusApiService
import com.example.busbookingapp.ui.theme.screens.dashboard.AnalyticsStat
import com.example.busbookingapp.analytics.AdminStatsResponse
class AnalyticsRepository(
    private val apiService: BusApiService
) {

    fun getDashboardStats(): Flow<List<AnalyticsStat>> = flow {
        while (true) {
            try {
                // 1. Fetch raw data from your backend
                val response = apiService.getAdminStats()

                // 2. Map backend data to UI models
                val uiStats = listOf(
                    AnalyticsStat(
                        label = "Today's Revenue",
                        value = "$${response.dailyRevenue}",
                        subValue = "${response.revenueGrowth}% vs yesterday",
                        icon = Icons.Default.Payments,
                        color = Color(0xFF4CAF50)
                    ),
                    AnalyticsStat(
                        label = "Avg. Occupancy",
                        value = "${response.occupancyRate}%",
                        subValue = "Trending up",
                        icon = Icons.Default.PieChart,
                        color = Color(0xFF2196F3)
                    )
                )

                emit(uiStats)
            } catch (e: Exception) {

            }
            delay(10000) // Poll every 10 seconds
        }
    }.flowOn(Dispatchers.IO) // Run on background thread
}