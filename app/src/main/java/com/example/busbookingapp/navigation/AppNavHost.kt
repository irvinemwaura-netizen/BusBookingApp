

package com.example.busbookingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.busbookingapp.ui.theme.screens.booking.BookingSuccessScreen
import com.example.busbookingapp.ui.theme.screens.dashboard.DashboardScreen
import com.example.busbookingapp.ui.theme.screens.home.HomeScreen
import com.example.busbookingapp.ui.theme.screens.login.LoginScreen
import com.example.busbookingapp.ui.theme.screens.register.RegisterScreen
import com.example.busbookingapp.ui.theme.screens.booking.SeatSelectionScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ROUTE_HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ROUTE_HOME) {
            HomeScreen(navController)
        }

        composable(Routes.ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(Routes.ROUTE_LOGIN) { LoginScreen(navController) }

        composable(Routes.ROUTE_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(
            route = "seat_selection/{time}/{price}",
            arguments = listOf(
                navArgument("time") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStack ->
            val time = backStack.arguments?.getString("time") ?: ""
            val price = backStack.arguments?.getString("price") ?: ""

            SeatSelectionScreen(navController, time, price)
        }
        composable(
            route = Routes.ROUTE_BOOKING,
            arguments = listOf(
                navArgument("plate") { type = NavType.StringType },
                navArgument("seat") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStack ->
            val plate = backStack.arguments?.getString("plate") ?: "N/A"
            val seat = backStack.arguments?.getString("seat") ?: "N/A"
            val time = backStack.arguments?.getString("time") ?: "N/A"
            val price = backStack.arguments?.getString("price") ?: "0"

            BookingSuccessScreen(
                navController = navController,
                busPlate = plate,
                seatNumber = seat,
                tripTime = time,
                amountPaid = "Ksh $price"
            )
        }
    }
}

