package com.example.busbookingapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.busbookingapp.data.AdminViewModel // IMPORT your AdminViewModel
import com.example.busbookingapp.data.AuthViewModel
import com.example.busbookingapp.ui.theme.screens.booking.BookingSuccessScreen
import com.example.busbookingapp.ui.theme.screens.booking.PaymentScreen
import com.example.busbookingapp.ui.theme.screens.dashboard.FullAdminDashboard // Use the Admin Dashboard
import com.example.busbookingapp.ui.theme.screens.home.HomeScreen
import com.example.busbookingapp.ui.theme.screens.login.LoginScreen
import com.example.busbookingapp.ui.theme.screens.register.RegisterScreen
import com.example.busbookingapp.ui.theme.screens.booking.SeatSelectionScreen
import com.example.busbookingapp.ui.theme.screens.buses.BusListScreen
import com.example.busbookingapp.ui.theme.screens.trips.MyTripsScreen
import com.example.busbookingapp.ui.theme.screens.profile.ProfileScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ROUTE_LOGIN, // Usually start at Login
    authViewModel: AuthViewModel = viewModel(),
    adminViewModel: AdminViewModel = viewModel() // Initialize AdminViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- Auth Screens ---
        composable(Routes.ROUTE_HOME) { HomeScreen(navController) }
        composable(Routes.ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(Routes.ROUTE_LOGIN) { LoginScreen(navController) }

        // --- Admin Dashboard ---
        composable(Routes.ROUTE_DASHBOARD) {
            // Passing the adminViewModel to handle live bus stats/analytics
            FullAdminDashboard(viewModel = adminViewModel)
        }

        // --- Passenger/User Screens ---
        composable(Routes.ROUTE_BUSSES) { BusListScreen(navController) }
        composable(Routes.ROUTE_TRIPS) { MyTripsScreen(navController) }

        composable(Routes.ROUTE_PROFILE) {
            ProfileScreen(navController, authViewModel)
        }

        // --- SEAT SELECTION ROUTE ---
        composable(
            route = "${Routes.ROUTE_SEAT_SELECTION}/{time}/{price}",
            arguments = listOf(
                navArgument("time") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStack ->
            val time = backStack.arguments?.getString("time") ?: ""
            val price = backStack.arguments?.getString("price") ?: ""
            SeatSelectionScreen(navController, time, price)
        }

        // --- PAYMENT SCREEN ROUTE ---
        composable(
            route = "${Routes.ROUTE_PAYMENT}/{plate}/{seat}/{time}/{price}",
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

            PaymentScreen(navController, plate, seat, time, price)
        }

        // --- BOOKING SUCCESS ROUTE ---
        composable(
            route = "${Routes.ROUTE_BOOKING}/{plate}/{seat}/{time}/{price}",
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