package com.example.busbookingapp.ui.theme.screens.booking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.busbookingapp.navigation.Routes
import java.util.*

@Composable
fun BookingSuccessScreen(
    navController: NavController,
    busPlate: String,
    seatNumber: String,
    tripTime: String,
    amountPaid: String
) {
    // Generate a random Transaction ID for the receipt
    val transactionId = remember { "TXN${(100000..999999).random()}BK" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Soft light background
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // 1. Success Header
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(70.dp)
        )
        Text(
            text = "Booking Confirmed!",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2D3436)
        )
        Text(
            text = "Your ticket is ready for travel",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 2. The Main Ticket Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // Trip Main Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(label = "BUS PLATE", value = busPlate)
                    DetailItem(label = "SEAT NO", value = seatNumber)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DetailItem(label = "DEPARTURE", value = tripTime)
                    DetailItem(label = "DATE", value = "May 4, 2026")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Ticket Divider
                TicketDashedDivider()

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Receipt Details
                Text(
                    text = "PAYMENT SUMMARY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                ReceiptRow(label = "Transaction ID", value = transactionId)
                ReceiptRow(label = "Payment Status", value = "Success", valueColor = Color(0xFF4CAF50))

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Paid", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        text = amountPaid,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF6200EE)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // QR Code Placeholder
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(100.dp),
                        tint = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 4. Action Button
        Button(
            onClick = {
                navController.navigate(Routes.ROUTE_DASHBOARD) {
                    popUpTo(Routes.ROUTE_HOME) { inclusive = false }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Back to Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

// Helper Components
@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(text = label, color = Color.LightGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
    }
}

@Composable
fun ReceiptRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = valueColor)
    }
}

@Composable
fun TicketDashedDivider() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}
