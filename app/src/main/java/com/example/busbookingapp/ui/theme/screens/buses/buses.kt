package com.example.busbookingapp.ui.theme.screens.buses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class BusRoute(val name: String, val brandColor: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusListScreen(navController: NavController) {
    val routes = listOf(
        BusRoute("Nairobi -- Eldoret", Color.Blue),
        BusRoute("Nairobi -- Mombasa", Color.Green),
        BusRoute("Nairobi -- Kisumu", Color.Red),
        BusRoute("Nairobi -- Nakuru", Color.Magenta),
        BusRoute("Nairobi -- Meru", Color.Magenta),
        BusRoute("Nairobi -- Nyeri", Color.Yellow),
        BusRoute("Nairobi -- Namanga", Color.Cyan),
        BusRoute("Nairobi -- Malindi", Color.Magenta),
        BusRoute("Nairobi -- Busia", Color.Blue), // Closest to Indigo
        BusRoute("Nairobi -- Kakamega", Color.Blue), // Closest to Light Blue
        BusRoute("Nairobi -- Kitale", Color.Green), // Closest to Lime
        BusRoute("Nairobi -- Garissa", Color.Red), // Closest to Orange
        BusRoute("Nairobi -- Narok", Color.Black), // Closest to Brown
        BusRoute("Nairobi -- Kericho", Color.Cyan),
        BusRoute("Nairobi -- Machakos", Color.Green),
        BusRoute("Nairobi -- Thika", Color.Gray),
        BusRoute("Nairobi -- Naivasha", Color.Yellow),
        BusRoute("Nairobi -- Voi", Color.Magenta) // Closest to Deep Purple
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fleet Manager", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF8FAFC)) // Match Light Theme Background
        ) {
            itemsIndexed(routes) { index, route ->
                BusRouteCard(index, route)
            }
        }
    }
}

@Composable
fun BusRouteCard(routeIndex: Int, route: BusRoute) {
    var expanded by remember { mutableStateOf(false) }
    val dashboardTimes = listOf("06:00 AM", "12:00 PM", "09:00 PM")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Header Section
            Row(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(route.brandColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = route.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                    Text(text = "6 active buses", fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }

            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F5F9))

                BusDirectionSection("OUTBOUND TO DESTINATION", routeIndex, 1, dashboardTimes)
                BusDirectionSection("INBOUND TO NAIROBI", routeIndex, 4, dashboardTimes)

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun BusDirectionSection(title: String, routeIndex: Int, startIdOffset: Int, times: List<String>) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp, start = 16.dp)
        ) {
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
        }

        times.forEachIndexed { i, time ->
            val busId = (routeIndex * 6) + i + startIdOffset
            BusItemRow("BUS-${busId.toString().padStart(3, '0')}", time)
        }
    }
}

@Composable
fun BusItemRow(busNo: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = busNo, fontSize = 14.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
        Text(text = time, fontSize = 14.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
    }
}