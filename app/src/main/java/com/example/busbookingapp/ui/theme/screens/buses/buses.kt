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
        BusRoute("Nairobi ↔ Eldoret", Color(0xFF2563EB)),
        BusRoute("Nairobi ↔ Mombasa", Color(0xFF16A34A)),
        BusRoute("Nairobi ↔ Kisumu", Color(0xFFDC2626)),
        BusRoute("Nairobi ↔ Nakuru", Color(0xFFEA580C)),
        BusRoute("Nairobi ↔ Meru", Color(0xFF9333EA)),
        BusRoute("Nairobi ↔ Nyeri", Color(0xFFCA8A04)),
        BusRoute("Nairobi ↔ Namanga", Color(0xFF0D9488)),
        BusRoute("Nairobi ↔ Malindi", Color(0xFFDB2777)),
        BusRoute("Nairobi ↔ Busia", Color(0xFF4F46E5)),
        BusRoute("Nairobi ↔ Kakamega", Color(0xFF0284C7)),
        BusRoute("Nairobi ↔ Kitale", Color(0xFF65A30D)),
        BusRoute("Nairobi ↔ Garissa", Color(0xFFF97316)),
        BusRoute("Nairobi ↔ Narok", Color(0xFF78350F)),
        BusRoute("Nairobi ↔ Kericho", Color(0xFF0891B2)),
        BusRoute("Nairobi ↔ Machakos", Color(0xFF84CC16)),
        BusRoute("Nairobi ↔ Thika", Color(0xFF475569)),
        BusRoute("Nairobi ↔ Naivasha", Color(0xFFEAB308)),
        BusRoute("Nairobi ↔ Voi", Color(0xFF7C3AED))
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