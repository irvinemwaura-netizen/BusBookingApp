package com.example.busbookingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.example.busbookingapp.navigation.Routes
import com.example.busbookingapp.ui.theme.screens.dashboard.DashboardScreen
import com.example.busbookingapp.ui.theme.screens.home.HomeScreen
import com.example.busbookingapp.ui.theme.screens.login.LoginScreen
import com.example.busbookingapp.ui.theme.screens.register.RegisterScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ROUTE_LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.ROUTE_LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.ROUTE_REGISTER) {
            RegisterScreen(navController)
        }

        composable(Routes.ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }
        composable ( Routes.ROUTE_HOME) {
            HomeScreen(navController)
        }
         }
}