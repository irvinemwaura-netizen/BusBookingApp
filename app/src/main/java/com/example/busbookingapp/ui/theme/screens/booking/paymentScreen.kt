package com.example.busbookingapp.ui.theme.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun PaymentScreen(
    navController: NavController,
    plate: String,
    seat: String,
    time: String,
    price: String,
    // paymentViewModel: PaymentViewModel = viewModel() // Ensure you have a ViewModel set up
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val backgroundColor = Color(0xFFF8FAFC)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Text("Checkout", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))

        Spacer(modifier = Modifier.height(20.dp))

        // ─── Ticket Summary ───
        TicketSummaryCard(plate, seat, time, price)

        Spacer(modifier = Modifier.height(30.dp))

        Text("M-PESA EXPRESS", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Phone Input
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { if (it.length <= 12) phoneNumber = it },
            label = { Text("M-Pesa Number (254...)") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isProcessing,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2563EB),
                unfocusedBorderColor = Color(0xFFCBD5E1)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your phone number to receive the M-Pesa PIN prompt.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = {
                if (phoneNumber.length >= 10) {
                    isProcessing = true


                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isProcessing && phoneNumber.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Waiting for PIN...")
            } else {
                Text("Pay Ksh $price", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TicketSummaryCard(plate: String, seat: String, time: String, price: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("TICKET SUMMARY", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Bus: $plate", color = Color(0xFF1E293B), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Seat: $seat", color = Color(0xFF64748B), fontSize = 16.sp)
            Text(time, color = Color(0xFF2563EB), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Amount", color = Color(0xFF1E293B))
                Text("Ksh $price", color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}