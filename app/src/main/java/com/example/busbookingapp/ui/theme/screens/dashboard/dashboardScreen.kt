package com.example.busbookingapp.ui.theme.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.busbookingapp.data.AuthViewModel

// ─── Data models ────────────────────────────────────────────────────────────
data class Departure(val time: String, val label: String, val seats: Int)
data class BusClass(val label: String, val price: Int, val description: String, val color: Color, val textColor: Color)
data class BusRoute(val name: String, val duration: String, val departures: List<Departure>, val classes: List<BusClass>)

// ─── Static Data (Light Theme Adjusted Colors) ───────────────────────────────
val defaultDepartures = listOf(
    Departure("06:00 AM", "Morning", 24),
    Departure("12:00 PM", "Afternoon", 18),
    Departure("09:00 PM", "Night", 12),
)

fun busClassesForRoute(luxuryPrice: Int, expressPrice: Int, standardPrice: Int) = listOf(
    BusClass("Luxury", luxuryPrice, "Reclining seats, AC, WiFi", Color(0xFFF0FDF4), Color(0xFF166534)),
    BusClass("Express", expressPrice, "AC, fast boarding", Color(0xFFEFF6FF), Color(0xFF1E40AF)),
    BusClass("Standard", standardPrice, "Affordable, reliable", Color(0xFFFFF7ED), Color(0xFF9A3412)),
)

val busRoutes = listOf(
    BusRoute("Nairobi -- Eldoret", "5 hrs", defaultDepartures, busClassesForRoute(1200, 1000, 700)),
    BusRoute("Nairobi -- Mombasa", "8 hrs", defaultDepartures, busClassesForRoute(2500, 2000, 1200)),
    BusRoute("Nairobi -- Kisumu", "6 hrs", defaultDepartures, busClassesForRoute(1800, 1500, 900)),
    BusRoute("Nairobi -- Nakuru", "2 hrs", defaultDepartures, busClassesForRoute(700, 600, 350)),
    BusRoute("Nairobi -- Meru", "4 hrs", defaultDepartures, busClassesForRoute(1000, 800, 500)),
    BusRoute("Nairobi -- Nyeri", "3 hrs", defaultDepartures, busClassesForRoute(800, 600, 400)),
    BusRoute("Nairobi -- Namanga", "3 hrs", defaultDepartures, busClassesForRoute(900, 700, 500)),
    BusRoute("Nairobi -- Malindi", "9 hrs", defaultDepartures, busClassesForRoute(2800, 2200, 1500)),
    BusRoute("Nairobi -- Busia", "7 hrs", defaultDepartures, busClassesForRoute(1800, 1400, 1000)),
)

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    // These states were already in your code, now we will actually use them
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredRoutes = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            busRoutes
        } else {
            busRoutes.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    val userName = authViewModel.currentUser?.displayName ?: "Traveler"
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(text = "Welcome, $userName ", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Text(text = "Book your next trip easily", color = Color(0xFF64748B), fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // FIX: Toggle the search bar visibility when clicked
            DashboardCard("Search", Icons.Default.Search) {
                isSearchVisible = !isSearchVisible
            }

            DashboardCard("My Trips", Icons.Default.History) {
                navController.navigate("trips_screen")
            }

            DashboardCard("Buses", Icons.Default.DirectionsBus) {
                navController.navigate("buses_screen")
            }
        }

        // ADDED: The actual Search Input field that appears when Search is clicked
        AnimatedVisibility(visible = isSearchVisible) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                placeholder = { Text("Where to? (e.g. Mombasa)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2563EB),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = if (searchQuery.isEmpty()) "Featured Routes" else "Search Results",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // FIX: Use 'filteredRoutes' instead of 'busRoutes'
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredRoutes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("No routes found for \"$searchQuery\"", color = Color.Gray, modifier = Modifier.padding(20.dp))
                    }
                }
            } else {
                items(filteredRoutes) { route ->
                    RouteCard(route = route, navController = navController)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { authViewModel.logout(navController, context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2), contentColor = Color(0xFF991B1B)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RouteCard(route: BusRoute, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<Departure?>(null) }
    var selectedClass by remember { mutableStateOf<BusClass?>(null) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(route.name, color = Color(0xFF1E293B), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(route.duration, color = Color(0xFF64748B), fontSize = 12.sp)
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Text("SELECT TIME", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 8.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(route.departures) { dep ->
                            DepartureChip(dep, selectedTime == dep) { selectedTime = dep }
                        }
                    }

                    Text("SELECT CLASS", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))

                    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        route.classes.forEach { busClass ->
                            BusClassCard(busClass, selectedClass == busClass) { selectedClass = busClass }
                        }
                    }

                    if (selectedTime != null && selectedClass != null) {
                        Button(
                            onClick = { navController.navigate("seat_selection/${selectedTime!!.time}/${selectedClass!!.price}") },
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Book ${selectedClass!!.label}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(100.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Color(0xFF2563EB))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color(0xFF1E293B), fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun DepartureChip(departure: Departure, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0xFFDBEAFE) else Color(0xFFF1F5F9)
    val textColor = if (isSelected) Color(0xFF1E40AF) else Color(0xFF475569)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(departure.time, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BusClassCard(busClass: BusClass, isSelected: Boolean, onClick: () -> Unit) {
    val borderStroke = if (isSelected) 2.dp else 1.dp
    val borderColor = if (isSelected) Color(0xFF2563EB) else Color(0xFFF1F5F9)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(busClass.color)
            .border(borderStroke, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(busClass.label, color = busClass.textColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(busClass.description, color = busClass.textColor.copy(alpha = 0.7f), fontSize = 11.sp)
            }
            Text("Ksh ${busClass.price}", color = busClass.textColor, fontWeight = FontWeight.ExtraBold)
        }
    }
}