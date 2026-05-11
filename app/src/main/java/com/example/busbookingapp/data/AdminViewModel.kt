package com.example.busbookingapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busbookingapp.analytics.AnalyticsRepository
import com.example.busbookingapp.models.AdminUser
import com.example.busbookingapp.ui.theme.screens.dashboard.AnalyticsStat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val analyticsRepo: AnalyticsRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<AnalyticsStat>>(emptyList())
    val uiState: StateFlow<List<AnalyticsStat>> = _uiState

    // State to track if an operation is currently loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchDashboardStats()
    }

    private fun fetchDashboardStats() {
        viewModelScope.launch {
            analyticsRepo.getDashboardStats().collect { stats ->
                _uiState.value = stats
            }
        }
    }

    // Fixed: Matches the function name in AuthRepository
    fun registerNewAdmin(admin: AdminUser, pass: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        authRepo.registerNewAdmin(admin, pass) { success, message ->
            _isLoading.value = false
            onResult(success, message)
        }
    }

    // login implementation
    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        _isLoading.value = true
        authRepo.login(email, pass) { success, message ->
            _isLoading.value = false
            onResult(success, message)
        }
    }
}