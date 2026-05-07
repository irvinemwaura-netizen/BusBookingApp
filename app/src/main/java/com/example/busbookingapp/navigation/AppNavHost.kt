package com.example.busbookingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.busbookingapp.ui.theme.screens.booking.BookingSuccessScreen
import com.example.busbookingapp.ui.theme.screens.booking.PaymentScreen
import com.example.busbookingapp.ui.theme.screens.dashboard.DashboardScreen
import com.example.busbookingapp.ui.theme.screens.home.HomeScreen
import com.example.busbookingapp.ui.theme.screens.login.LoginScreen
import com.example.busbookingapp.ui.theme.screens.register.RegisterScreen
import com.example.busbookingapp.ui.theme.screens.booking.SeatSelectionScreen
import com.example.busbookingapp.ui.theme.screens.buses.BusListScreen
import com.example.busbookingapp.ui.theme.screens.trips.MyTripsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.ROUTE_HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ROUTE_HOME) { HomeScreen(navController) }
        composable(Routes.ROUTE_REGISTER) { RegisterScreen(navController) }
        composable(Routes.ROUTE_LOGIN) { LoginScreen(navController) }
        composable(Routes.ROUTE_DASHBOARD) { DashboardScreen(navController) }

        // --- Fleet Manager / Buses ---
        composable(Routes.ROUTE_TRIPS) {
            BusListScreen(navController)
        }

        // --- My Bookings ---
        composable(Routes.ROUTE_BUSSES) { // Note: Your object has ROUTE_BUSSES for the trips list
            MyTripsScreen(navController)
        }

        // --- Seat Selection ---
        composable(
            route = Routes.ROUTE_SEAT_SELECTION,
            arguments = listOf(
                navArgument("time") { type = NavType.StringType },
                navArgument("price") { type = NavType.StringType }
            )
        ) { backStack ->
            val time = backStack.arguments?.getString("time") ?: ""
            val price = backStack.arguments?.getString("price") ?: ""
            SeatSelectionScreen(navController, time, price)
        }

        // --- Payment ---
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

        // --- Booking Success ---
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