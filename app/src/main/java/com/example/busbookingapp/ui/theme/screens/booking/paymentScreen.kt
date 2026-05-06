package com.example.busbookingapp.ui.theme.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    price: String
) {
    var selectedMethod by remember { mutableStateOf("M-Pesa") }
    // State for the phone number input
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        Text("Checkout", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

        Spacer(modifier = Modifier.height(20.dp))

        // ─── Ticket Summary Card ───
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("TICKET SUMMARY", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Bus: $plate", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Seat: $seat", color = Color.White, fontSize = 16.sp)
                Text(time, color = Color.Red, fontSize = 16.sp)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount", color = Color.White)
                    Text("Ksh $price", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text("SELECT PAYMENT METHOD", color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))

        // ─── Payment Options ───
        val methods = listOf("M-Pesa", "Credit Card", "Google Pay")
        methods.forEach { method ->
            PaymentMethodItem(
                name = method,
                isSelected = selectedMethod == method,
                onClick = { selectedMethod = method }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }


        if (selectedMethod == "M-Pesa") {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 12) phoneNumber = it },
                label = { Text("M-Pesa Number (e.g. 2547...)", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.Red
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = {
                if (selectedMethod == "M-Pesa") {
                    if (phoneNumber.length < 10) {
                        return@Button
                    }


                    val encodedPlate = URLEncoder.encode(plate, StandardCharsets.UTF_8.toString())
                    val encodedTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString())
                    navController.navigate("booking_success/$encodedPlate/$seat/$encodedTime/$price")
                } else {
                    val encodedPlate = URLEncoder.encode(plate, StandardCharsets.UTF_8.toString())
                    val encodedTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString())
                    navController.navigate("booking_success/$encodedPlate/$seat/$encodedTime/$price")
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Pay Ksh $price", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PaymentMethodItem(name: String, isSelected: Boolean, onClick: () -> Unit) {
}