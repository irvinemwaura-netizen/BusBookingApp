package com.example.busbookingapp.ui.theme.screens.booking



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.busbookingapp.navigation.Routes

@Composable
fun SeatSelectionScreen(
    navController: NavController,
    tripTime: String,
    tripPrice: String
) {
    // State to track which seat is clicked
    var selectedSeat by remember { mutableStateOf<Int?>(null) }

    // Total seats in the bus
    val totalSeats = 44

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark background
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Seat",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Trip: $tripTime • Ksh $tripPrice",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SeatLegend(Color.DarkGray, "Available")
            SeatLegend(Color.Red, "Selected")
            SeatLegend(Color(0xFF2A2A2A), "Booked")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Bus Floor/Grid
        Box(
            modifier = Modifier
                .weight(1f)
                .width(280.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color(0xFF1E1E1E))
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // 2 seats - aisle - 2 seats
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(totalSeats) { index ->
                    val seatNum = index + 1
                    val isBooked = seatNum % 5 == 0

                    SeatIcon(
                        number = seatNum,
                        isSelected = selectedSeat == seatNum,
                        isBooked = isBooked,
                        onClick = { if (!isBooked) selectedSeat = seatNum }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm Button
        Button(
            onClick = {
                if (selectedSeat != null) {
                    val plate = "KCD ${(100..999).random()}X"
                    navController.navigate("payment_screen/$plate/$selectedSeat/$tripTime/$tripPrice")
                }
            },
            enabled = selectedSeat != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                disabledContainerColor = Color.DarkGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (selectedSeat != null) "Confirm Seat $selectedSeat" else "Select a Seat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun SeatIcon(
    number: Int,
    isSelected: Boolean,
    isBooked: Boolean,
    onClick: () -> Unit
) {
    val bgColor = when {
        isBooked -> Color(0xFF2A2A2A) // Dark/Disabled
        isSelected -> Color.Red         // Highlighted
        else -> Color.DarkGray        // Default
    }

    val borderColor = if (isSelected) Color.White else Color.Transparent

    Box(
        modifier = Modifier
            .size(45.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable(enabled = !isBooked) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = if (isBooked) Color.Gray else Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SeatLegend(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, color = Color.Gray, fontSize = 12.sp)
    }
}