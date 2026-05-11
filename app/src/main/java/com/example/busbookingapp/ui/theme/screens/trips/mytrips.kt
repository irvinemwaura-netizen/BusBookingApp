package com.example.busbookingapp.ui.theme.screens.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.busbookingapp.navigation.Routes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Model for the trip data
data class BookedTrip(
    val busId: String,
    val route: String,
    val date: String,
    val time: String,
    val seat: String,
    val price: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripsScreen(navController: NavController) {
    val backgroundColor = Color(0xFFF8FAFC)

    // State-managed list to ensure UI refreshes on deletion
    val myBookings = remember {
        mutableStateListOf<BookedTrip>(
            BookedTrip("KDA-123", "Nairobi ↔ Eldoret", "May 12, 2026", "06:00 AM", "12A", "1,200", "Upcoming"),
            BookedTrip("KCB-456", "Mombasa ↔ Nairobi", "April 20, 2026", "09:00 PM", "05B", "2,500", "Completed"),
            BookedTrip("KDL-789", "Nairobi ↔ Nakuru", "April 05, 2026", "12:00 PM", "22", "700", "Completed")
        )
    }

    // State for the delete confirmation dialog
    var bookingToDelete by remember { mutableStateOf<BookedTrip?>(null) }

    // --- DELETE CONFIRMATION DIALOG ---
    bookingToDelete?.let { trip ->
        AlertDialog(
            onDismissRequest = { bookingToDelete = null },
            title = { Text("Cancel Booking") },
            text = { Text("Are you sure you want to cancel seat ${trip.seat} for the ${trip.route} trip?") },
            confirmButton = {
                Button(
                    onClick = {
                        myBookings.remove(trip)
                        bookingToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
                ) { Text("Confirm", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { bookingToDelete = null }) { Text("Keep Trip") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(myBookings) { booking ->
                LightBookingCard(
                    booking = booking,
                    onDelete = { bookingToDelete = booking },
                    onViewTicket = {
                        // --- APPROACH A: SKIP PAYMENT & GO TO TICKET ---
                        // We encode the details so the NavHost can parse them safely
                        val plate = URLEncoder.encode(booking.busId, StandardCharsets.UTF_8.toString())
                        val seat = URLEncoder.encode(booking.seat, StandardCharsets.UTF_8.toString())
                        val time = URLEncoder.encode(booking.time, StandardCharsets.UTF_8.toString())
                        val price = URLEncoder.encode(booking.price, StandardCharsets.UTF_8.toString())

                        // Navigating to the Success/Ticket route pattern
                        navController.navigate("${Routes.ROUTE_BOOKING}/$plate/$seat/$time/$price")
                    }
                )
            }
        }
    }
}

@Composable
fun LightBookingCard(
    booking: BookedTrip,
    onDelete: () -> Unit,
    onViewTicket: () -> Unit
) {
    val isUpcoming = booking.status == "Upcoming"
    val statusColor = if (isUpcoming) Color(0xFF2E7D32) else Color(0xFF64748B)
    val statusBg = if (isUpcoming) Color(0xFFE8F5E9) else Color(0xFFF1F5F9)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(color = statusBg, shape = RoundedCornerShape(20.dp)) {
                    Text(
                        text = booking.status,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Show action buttons only for Upcoming trips
                if (isUpcoming) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = onViewTicket) {
                            Text("View Ticket", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = booking.route,
                color = Color(0xFF1E293B),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LightDetailColumn("DATE", booking.date)
                LightDetailColumn("TIME", booking.time)
                LightDetailColumn("SEAT", booking.seat)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer displaying Ticket Status and Price
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isUpcoming) Color(0xFFF0F7FF) else Color(0xFFF8FAFC),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = if (isUpcoming) Color(0xFF2563EB) else Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUpcoming) "Ticket Active" else "Trip Completed",
                            color = if (isUpcoming) Color(0xFF1E40AF) else Color(0xFF64748B),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "Ksh ${booking.price}",
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LightDetailColumn(label: String, value: String) {
    Column {
        Text(text = label, color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = Color(0xFF334155), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}