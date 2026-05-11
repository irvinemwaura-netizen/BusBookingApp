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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.busbookingapp.api.TokenResponse
import com.example.busbookingapp.api.RetrofitClient
import com.example.busbookingapp.api.StkPushRequest
import com.example.busbookingapp.navigation.Routes
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


    val consumerKey = "xaMIsjJNO4XX0kuVcbsNecLCN5NGOSySzDjT0VETjdfuaWth"
    val consumerSecret = "Z7Cn3vFHyaIRH28T8HA4bXQsluyly3rSxO33UDl4c8JRhAAlhQikZvEzov6zQX0g"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Text("Checkout", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))

        Spacer(modifier = Modifier.height(20.dp))

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
            text = "Enter your number to receive an M-Pesa PIN prompt.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (phoneNumber.length >= 10) {
                    isProcessing = true
                    coroutineScope.launch {
                        try {
                            // 1. Format the phone number for M-Pesa (254...)
                            val cleanNumber = phoneNumber.replace("\\s".toRegex(), "")
                            val formattedPhone = when {
                                cleanNumber.startsWith("0") -> "254" + cleanNumber.substring(1)
                                cleanNumber.startsWith("+") -> cleanNumber.substring(1)
                                cleanNumber.startsWith("254") -> cleanNumber
                                else -> "254$cleanNumber"
                            }

                            // 2. Prepare Auth Header & Generate Token
                            val authHeader = "Basic " + android.util.Base64.encodeToString(
                                "$consumerKey:$consumerSecret".toByteArray(),
                                android.util.Base64.NO_WRAP
                            )

                            val tokenResponse = RetrofitClient.mpesaService.generateToken(authHeader)

                            if (tokenResponse.isSuccessful) {
                                val accessToken = tokenResponse.body()?.accessToken
                                if (accessToken != null) {
                                    val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
                                    val shortCode = "174379"
                                    val passkey = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
                                    val password = android.util.Base64.encodeToString(
                                        "$shortCode$passkey$timestamp".toByteArray(),
                                        android.util.Base64.NO_WRAP
                                    )

                                    val request = StkPushRequest(
                                        BusinessShortCode = shortCode,
                                        Password = password,
                                        Timestamp = timestamp,
                                        TransactionType = "CustomerPayBillOnline",
                                        Amount = price.filter { it.isDigit() }.ifEmpty { "1" },
                                        PartyA = formattedPhone,
                                        PartyB = shortCode,
                                        PhoneNumber = formattedPhone,
                                        CallBackURL = "https://yourdomain.com/callback",
                                        AccountReference = "BusBookingApp",
                                        TransactionDesc = "Seat $seat Payment"
                                    )

                                    // Triggers the M-Pesa Pin Prompt
                                    RetrofitClient.mpesaService.initiateStkPush("Bearer $accessToken", request)
                                }
                            }

                            // 3. THE DEMO BYPASS: Wait for 5 seconds to simulate processing
                            kotlinx.coroutines.delay(5000)

                        } catch (e: Exception) {
                            Log.e("NetworkError", "Exception: ${e.message}")
                            kotlinx.coroutines.delay(2000)
                        } finally {
                            // 4. NAVIGATION & CLEANUP
                            // Use URLEncoder for values like plate (KCB 123) or time (10:00 PM)
                            val encodedPlate = URLEncoder.encode(plate, "UTF-8")
                            val encodedSeat = URLEncoder.encode(seat, "UTF-8")
                            val encodedTime = URLEncoder.encode(time, "UTF-8")
                            val encodedPrice = URLEncoder.encode(price, "UTF-8")

                            // Build the destination using your Route Constant
                            val destination = "${Routes.ROUTE_BOOKING}/$encodedPlate/$encodedSeat/$encodedTime/$encodedPrice"

                            navController.navigate(destination) {
                                // Clears the payment screen so user can't go back to it
                                popUpTo(Routes.ROUTE_DASHBOARD) { inclusive = false }
                            }
                            isProcessing = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isProcessing // Disable button while processing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Pay $price")
            }
        }
    }
}

@Composable
fun TicketSummaryCard(plate: String, seat: String, time: String, price: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Trip Summary", color = Color(0xFF64748B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("BUS PLATE", color = Color.Gray, fontSize = 10.sp)
                    Text(plate, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("SEAT", color = Color.Gray, fontSize = 10.sp)
                    Text(seat, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("TIME", color = Color.Gray, fontSize = 10.sp)
                    Text(time, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("TOTAL PRICE", color = Color.Gray, fontSize = 10.sp)
                    Text(price, color = Color(0xFF2563EB), fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        navController = rememberNavController(),
        plate = "KDA 123A",
        seat = "A12",
        time = "10:30 AM",
        price = "Ksh 1500"
    )
}