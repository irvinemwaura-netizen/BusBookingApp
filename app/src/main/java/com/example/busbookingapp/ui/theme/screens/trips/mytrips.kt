package com.example.busbookingapp.ui.theme.screens.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// 1. ADDED DATA CLASS: This fixes the "Unresolved reference 'BookedTrip'" errors
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

    val myBookings = listOf(
        BookedTrip("BUS-001", "Nairobi ↔ Eldoret", "May 12, 2026", "06:00 AM", "12A", "Ksh 1,200", "Upcoming"),
        BookedTrip("BUS-042", "Mombasa ↔ Nairobi", "April 20, 2026", "09:00 PM", "05B", "Ksh 2,500", "Completed"),
        BookedTrip("BUS-015", "Nairobi ↔ Nakuru", "April 05, 2026", "12:00 PM", "22", "Ksh 700", "Completed")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings", color = Color.Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // FIXED: Using AutoMirrored version to satisfy the deprecation warning
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
            // FIXED: Type T is now inferred because BookedTrip is defined
            items(myBookings) { booking ->
                LightBookingCard(booking)
            }
        }
    }
}

@Composable
fun LightBookingCard(booking: BookedTrip) {
    val statusColor = if (booking.status == "Upcoming") Color(0xFF2E7D32) else Color(0xFF64748B)
    val statusBg = if (booking.status == "Upcoming") Color(0xFFE8F5E9) else Color(0xFFF1F5F9)

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
                Text(text = booking.busId, color = Color(0xFF64748B), fontSize = 12.sp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F7FF), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // FIXED: Icons do not have a 'size' parameter. Use Modifier.size() instead.
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ticket Active", color = Color(0xFF1E40AF), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Text(text = booking.price, color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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