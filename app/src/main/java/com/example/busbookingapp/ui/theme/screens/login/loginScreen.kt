package com.example.busbookingapp.ui.theme.screens.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.busbookingapp.R
import com.example.busbookingapp.data.AuthViewModel
import com.example.busbookingapp.navigation.Routes.ROUTE_REGISTER
import com.example.busbookingapp.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

 @Composable
 fun LoginScreen(navController: NavController) {
                                    val authViewModel: AuthViewModel = viewModel()
                                    var email by remember { mutableStateOf("") }
                                    var password by remember { mutableStateOf("") }
                                    val context = LocalContext.current

                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Image(
                                            painter = painterResource(id = R.drawable.logo),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.7f))
                                        )

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(16.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.bg),
                                                contentDescription = "Logo",
                                                modifier = Modifier
                                                    .size(140.dp)
                                                    .clip(CircleShape)
                                                    .border(2.dp, Color.Gray, CircleShape)
                                                    .shadow(4.dp, CircleShape)
                                            )
                                            Text(
                                                text = "Login",
                                                color = Color.Gray,
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(bottom = 24.dp)
                                            )

                                            // Email and Password Fields (Same as your previous code)
                                            OutlinedTextField(
                                                value = email,
                                                onValueChange = { email = it },
                                                label = { Text("Email", color = Color.White) },
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.White,
                                                    unfocusedTextColor = Color.White,
                                                    focusedBorderColor = Color.Green,
                                                    unfocusedBorderColor = Color.Gray
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            OutlinedTextField(
                                                value = password,
                                                onValueChange = { password = it },
                                                label = { Text("Password", color = Color.White) },
                                                visualTransformation = PasswordVisualTransformation(),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.White,
                                                    unfocusedTextColor = Color.White,
                                                    focusedBorderColor = Color.Green,
                                                    unfocusedBorderColor = Color.Gray
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // USER LOGIN BUTTON
                                            Button(
                                                onClick = {
                                                    if (email.isBlank() || password.isBlank()) {
                                                        Toast.makeText(context, "Email and password required", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        authViewModel.login(email, password, navController, context)
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                            ) {
                                                Text("Login as Passenger", color = Color.White)
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            // --- ADMIN LOGIN OPTION ---
                                            Text(
                                                "Login as Admin",
                                                color = Color.Yellow.copy(alpha = 0.8f),
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier
                                                    .clickable {
                                                        if (email.isBlank() || password.isBlank()) {
                                                            Toast.makeText(context, "Admin credentials required", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            // You can add a specific adminLogin function in your ViewModel
                                                            // or pass a flag to the existing login function
                                                            authViewModel.adminLogin(email, password, navController, context)
                                                        }
                                                    }
                                                    .padding(8.dp)
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            Row {
                                                Text("Don't have an account?", color = Color.White)
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Text(
                                                    "Register Here",
                                                    color = Color.Green,
                                                    modifier = Modifier.clickable { navController.navigate(Routes.ROUTE_REGISTER) }
                                                )
                                            }
                                        }
                                    }
                                }

    @Preview(showBackground = true)
    @Composable
    fun LoginScreenPreview() {
        LoginScreen(rememberNavController())
    }

