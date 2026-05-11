package com.example.busbookingapp.ui.theme.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import com.example.busbookingapp.data.AdminViewModel // Ensure this path is correct
import androidx.compose.runtime.getValue // For 'by remember'
import androidx.compose.runtime.setValue // For 'by remember'
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. ENHANCED DATA MODELS ---
data class AnalyticsStat(
    val label: String,
    val value: String,
    val subValue: String, // e.g., "80% Occupancy"
    val icon: ImageVector,
    val color: Color
)

data class Bus(
    val id: String,
    val type: String, // AC, Sleeper, Seater
    val plate: String,
    val status: String // Active, Maintenance
)

data class Route(
    val id: String,
    val origin: String,
    val destination: String,
    val stops: Int
)

// --- 2. MAIN ADMIN DASHBOARD ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullAdminDashboard(viewModel: AdminViewModel) {
    var showAddBusDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    // Fix: provide initial value to help compiler infer type
    val stats by viewModel.uiState.collectAsState(initial = emptyList())

    val tabs = listOf("Overview", "Fleet", "Routes", "Staff")

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                CenterAlignedTopAppBar(
                    title = { Text("BusOps Command Center", fontWeight = FontWeight.ExtraBold) },
                    // Fix: Use topAppBarColors instead of centerAlignedTopAppBarColors
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                // Fix: Use PrimaryScrollableTabRow
                PrimaryScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 16.dp,
                    containerColor = Color.Transparent
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) } // Text should be in the 'text' parameter
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                ExtendedFloatingActionButton(
                    onClick = { showAddBusDialog = true },
                    // Use DirectionsBus (ensure you have the extended library)
                    icon = { Icon(Icons.Default.DirectionsBus, contentDescription = null) },
                    text = { Text("Register Bus") }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            when (selectedTab) {
                0 -> AnalyticsSection(stats) // Use the live stats from viewModel
                1 -> FleetManagementSection()
                2 -> RouteManagementSection()
                3 -> StaffManagementSection()
            }
        }
    }

    if (showAddBusDialog) {
        AddBusDialog(onDismiss = { showAddBusDialog = false })
    }
}

// --- 3. SUB-SECTIONS ---

@Composable
fun AnalyticsSection(stats: List<AnalyticsStat>) {
    Text("Live Performance", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stats) { stat ->
            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(stat.icon, contentDescription = null, tint = stat.color)
                    Text(stat.value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(stat.label, style = MaterialTheme.typography.bodyMedium)
                    Text(stat.subValue, style = MaterialTheme.typography.labelSmall, color = stat.color)
                }
            }
        }
    }
}

@Composable
fun FleetManagementSection() {
    val buses = listOf(
        Bus("1", "AC Sleeper", "KCB 001A", "Active"),
        Bus("2", "Luxury Seater", "KCC 999B", "Maintenance"),
        Bus("3", "Standard Seater", "KDA 555C", "Active")
    )

    Text("Fleet & Inventory", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 16.dp)) {
        items(buses) { bus ->
            ListItem(
                headlineContent = { Text(bus.plate) },
                supportingContent = { Text("${bus.type} • Status: ${bus.status}") },
                leadingContent = { Icon(Icons.Default.DirectionsBus, contentDescription = null) },
                trailingContent = {
                    Switch(checked = bus.status == "Active", onCheckedChange = {})
                },
                tonalElevation = 2.dp,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun RouteManagementSection() {
    val routes = listOf(
        Route("1", "Nairobi", "Mombasa", 4),
        Route("2", "Kisumu", "Nairobi", 2)
    )
    Text("Route Logistics", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
        items(routes) { route ->
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${route.origin} → ${route.destination}", fontWeight = FontWeight.Bold)
                        Text("${route.stops} Intermediate Stops", style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { /* Edit Route */ }) {
                        Icon(Icons.Default.EditLocation, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun StaffManagementSection() {
    Text("Roles & Permissions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    // Display different staff types: Super Admin, Counter Staff, Driver
    val staff = listOf("John (Driver)", "Sarah (Counter)", "Mike (Super Admin)")
    Column(modifier = Modifier.padding(top = 16.dp)) {
        staff.forEach { person ->
            Text("• $person", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun AddBusDialog(onDismiss: () -> Unit) {
    var busType by remember { mutableStateOf("AC Seater") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Vehicle") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = "", onValueChange = {}, label = { Text("Plate Number") })
                // Simple Text toggle for types
                Row {
                    listOf("AC", "Sleeper", "Seater").forEach { type ->
                        FilterChip(
                            selected = busType.contains(type),
                            onClick = { busType = type },
                            label = { Text(type) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Save Bus") } }
    )
}
