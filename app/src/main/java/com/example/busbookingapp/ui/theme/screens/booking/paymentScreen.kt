package com.example.busbookingapp.ui.theme.screens.booking

import android.util.Base64
import android.util.Log
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
import com.example.busbookingapp.api.RetrofitClient
import com.example.busbookingapp.api.StkPushRequest
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentScreen(
    navController: NavController,
    plate: String,
    seat: String,
    time: String,
    price: String
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val backgroundColor = Color(0xFFF8FAFC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Text("Checkout", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))

        Spacer(modifier = Modifier.height(20.dp))

        // Ticket Summary
        TicketSummaryCard(plate, seat, time, price)

        Spacer(modifier = Modifier.height(30.dp))

        Text("M-PESA EXPRESS", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { if (it.length <= 12) phoneNumber = it },
            label = { Text("M-Pesa Number (e.g. 0712...)") },
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
            text = "You will receive an M-Pesa PIN prompt on your phone.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))

        // ─── THE PAYMENT BUTTON ───
        Button(
            onClick = {
                if (phoneNumber.length >= 10) {
                    isProcessing = true
                    coroutineScope.launch {
                        try {
                            // 1. Format Phone Number (254...)
                            val formattedPhone = when {
                                phoneNumber.startsWith("0") -> "254" + phoneNumber.substring(1)
                                phoneNumber.startsWith("+") -> phoneNumber.substring(1)
                                phoneNumber.startsWith("7") || phoneNumber.startsWith("1") -> "254$phoneNumber"
                                else -> phoneNumber
                            }

                            // 2. Generate Timestamp & Password
                            val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                            val shortCode = "174379" // Sandbox Shortcode
                            val passkey = "bfb3c64829623dc11fb49e006a48a44174379bfb3c64829623dc11fb49e006a48"
                            val password = Base64.encodeToString(
                                "$shortCode$passkey$timestamp".toByteArray(),
                                Base64.NO_WRAP
                            )

                            val request = StkPushRequest(
                                BusinessShortCode = shortCode,
                                Password = password,
                                Timestamp = timestamp,
                                TransactionType = "CustomerPayBillOnline",
                                Amount = price,
                                PartyA = formattedPhone,
                                PartyB = shortCode,
                                PhoneNumber = formattedPhone,
                                CallBackURL = "https://yourdomain.com/callback", // Must be HTTPS
                                AccountReference = "BusBooking",
                                TransactionDesc = "Payment for Seat $seat"
                            )

                            // 4. Execute network call (Replace TOKEN with your dynamic/portal token)
                            val response = RetrofitClient.mpesaService.initiateStkPush(
                                bearerToken = "Bearer PASTE_YOUR_ACCESS_TOKEN_HERE",
                                request = request
                            )

                            if (response.isSuccessful) {
                                // Success! Navigate to confirmation
                                val encodedPlate = URLEncoder.encode(plate, StandardCharsets.UTF_8.toString())
                                navController.navigate("success_screen/$encodedPlate/$seat")
                            } else {
                                Log.e("MpesaError", "Response Error: ${response.errorBody()?.string()}")
                                isProcessing = false
                            }

                        } catch (e: Exception) {
                            Log.e("NetworkError", "Failed to connect: ${e.message}")
                            isProcessing = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isProcessing && phoneNumber.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sending Prompt...")
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