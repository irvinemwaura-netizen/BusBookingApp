package com.example.busbookingapp.ui.theme.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.busbookingapp.data.AuthViewModel

// ─── Data models ────────────────────────────────────────────────────────────

data class Departure(
    val time: String,
    val label: String,
    val seats: Int
)

data class BusClass(
    val label: String,
    val price: Int,
    val description: String,
    val color: Color,
    val textColor: Color
)

data class BusRoute(
    val name: String,
    val duration: String,
    val departures: List<Departure>,
    val classes: List<BusClass>
)

// ─── Static data ─────────────────────────────────────────────────────────────

val defaultDepartures = listOf(
    Departure("06:00 AM", "Morning",   24),
    Departure("12:00 PM", "Afternoon", 18),
    Departure("09:00 PM", "Night",     12),
)

fun busClassesForRoute(
    luxuryPrice: Int,
    expressPrice: Int,
    nightPrice: Int,
    standardPrice: Int
) = listOf(
    BusClass("Luxury",   luxuryPrice,   "Reclining seats, AC, WiFi", Color(0xFF1A2A1A), Color(0xFF7DDC8A)),
    BusClass("Express",  expressPrice,  "AC, fast boarding",          Color(0xFF1A1A2A), Color(0xFF7AAAF5)),
    BusClass("Night",    nightPrice,    "Overnight, blanket incl.",   Color(0xFF2A1A2A), Color(0xFFC07AF5)),
    BusClass("Standard", standardPrice, "Affordable, reliable",       Color(0xFF2A2A1A), Color(0xFFF5C07A)),
)

val busRoutes = listOf(
    BusRoute("Nairobi → Mombasa", "8 hrs", defaultDepartures, busClassesForRoute(2500, 2000, 1800, 1200)),
    BusRoute("Nairobi → Kisumu",  "6 hrs", defaultDepartures, busClassesForRoute(1800, 1500, 1300,  900)),
    BusRoute("Nairobi → Nakuru",  "2 hrs", defaultDepartures, busClassesForRoute( 700,  600,  500,  350)),
    BusRoute("Mombasa → Malindi", "2 hrs", defaultDepartures, busClassesForRoute( 600,  500,  450,  300)),
    BusRoute("Nairobi → Eldoret", "5 hrs", defaultDepartures, busClassesForRoute(1200, 1000,  900,  700)),
)

// ─── Screen ──────────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome 👋",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Book your next trip easily",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Quick-action cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DashboardCard("Search",   Icons.Default.Search)        {}
            DashboardCard("My Trips", Icons.Default.History)       {}
            DashboardCard("Buses",    Icons.Default.DirectionsBus) {}
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Featured buses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Scrollable route list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(busRoutes) { route ->
                RouteCard(route = route)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        Button(
            onClick = { authViewModel.logout(navController, context) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

// ─── Route card ──────────────────────────────────────────────────────────────

@Composable
fun RouteCard(route: BusRoute) {
    var expanded        by remember { mutableStateOf(false) }
    var selectedTime    by remember { mutableStateOf<Departure?>(null) }
    var selectedClass   by remember { mutableStateOf<BusClass?>(null) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Header — tap to expand/collapse
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = route.name,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${route.duration} • 3 departures daily",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            // Expandable content
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(color = Color(0xFF2A2A2A))

                    // Departure times
                    Text(
                        text = "SELECT DEPARTURE TIME",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 6.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(route.departures) { dep ->
                            DepartureChip(
                                departure = dep,
                                isSelected = selectedTime == dep,
                                onClick = { selectedTime = dep }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF2A2A2A))

                    // Bus classes
                    Text(
                        text = "SELECT CLASS",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 6.dp)
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Two columns via chunked rows
                        route.classes.chunked(2).forEach { rowClasses ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowClasses.forEach { busClass ->
                                    BusClassCard(
                                        busClass = busClass,
                                        isSelected = selectedClass == busClass,
                                        modifier = Modifier.weight(1f),
                                        onClick = { selectedClass = busClass }
                                    )
                                }
                                // Fill remaining space if odd number
                                if (rowClasses.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // Book button — only when both time and class are chosen
                    if (selectedTime != null && selectedClass != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { /* TODO: navigate to booking screen */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "Book ${selectedClass!!.label} • ${selectedTime!!.time} — Ksh ${"%,d".format(selectedClass!!.price)}",
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

// ─── Departure time chip ──────────────────────────────────────────────────────

@Composable
fun DepartureChip(
    departure: Departure,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color.Red else Color.Transparent
    val bgColor     = if (isSelected) Color(0xFF2A1A1A) else Color(0xFF2A2A2A)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = departure.time,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${departure.label} • ${departure.seats} seats",
            color = if (isSelected) Color(0xFFFF8888) else Color.Gray,
            fontSize = 11.sp
        )
    }
}

// ─── Bus class card ───────────────────────────────────────────────────────────

@Composable
fun BusClassCard(
    busClass: BusClass,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color.Red else Color.Transparent

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(busClass.color)
            .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(
            text = busClass.label,
            color = busClass.textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Ksh ${"${busClass.price}".let {
                it.reversed().chunked(3).joinToString(",").reversed()
            }}",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = busClass.description,
            color = busClass.textColor.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
    }
}

// ─── Quick action card ────────────────────────────────────────────────────────

@Composable
fun DashboardCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White)
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(rememberNavController())
}