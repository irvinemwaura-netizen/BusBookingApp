package com.example.busbookingapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.example.busbookingapp.ui.theme.screens.dashboard.Bus
import com.example.busbookingapp.ui.theme.screens.dashboard.Route

class BusRepository {
    private val db = FirebaseFirestore.getInstance()

    // Save a new Bus
    fun addBus(bus: Bus, onResult: (Boolean) -> Unit) {
        db.collection("buses")
            .document(bus.plate) // Using plate as unique ID
            .set(bus)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Save a new Route
    fun addRoute(route: Route, onResult: (Boolean) -> Unit) {
        db.collection("routes")
            .add(route) // Let Firestore auto-generate the ID
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}