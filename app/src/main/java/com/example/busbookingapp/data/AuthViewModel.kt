package com.example.busbookingapp.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.busbookingapp.models.UserModel
import com.example.busbookingapp.navigation.Routes.ROUTE_DASHBOARD
import com.example.busbookingapp.navigation.Routes.ROUTE_LOGIN
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    val currentUser: com.google.firebase.auth.FirebaseUser?
        get() = auth.currentUser


    fun signup(   username: String,
                  name: String,
                  email: String,
                  password: String,
                  confirmPassword: String,
                  navController: NavController,
                  context: Context,
                  phoneNumber: String
    ) {
        if (username.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid ?: ""
                val user = UserModel(username = username, phoneNumber = phoneNumber, name = name,email = email, userId = userId)
                saveUserToDatabase(user, navController, context)
            } else {
                Toast.makeText(context, task.exception?.message ?: "Registration failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserToDatabase(user: UserModel, navController: NavController, context: Context) {
        val dbRef = FirebaseDatabase.getInstance().getReference("User/${user.userId}")
        dbRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User Registered successfully", Toast.LENGTH_LONG).show()
                // Navigate safely to login without crashing
                navController.navigate(ROUTE_LOGIN) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            } else {
                Toast.makeText(context, task.exception?.message ?: "Failed to save user", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(
        email: String,
        password: String,
        navController: NavController,
        context: Context
    ) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and Password are required", Toast.LENGTH_LONG).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()

                    navController.navigate(ROUTE_DASHBOARD) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }

                } else {
                    Toast.makeText(
                        context,
                        task.exception?.message ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
    fun logout(navController: NavController, context: Context) {
        auth.signOut()
        Toast.makeText(context, "Logout Successful", Toast.LENGTH_LONG).show()
        navController.navigate(ROUTE_LOGIN) {
            popUpTo(ROUTE_DASHBOARD) { inclusive = true }
            launchSingleTop = true
        }
    }
    fun adminLogin(email: String, pass: String, navController: NavController, context: android.content.Context) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""

                db.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        val userRole = document.getString("role")
                        if (userRole == "admin") {
                            Toast.makeText(context, "Admin Login Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("admin_dashboard") // Replace with your actual route
                        } else {
                            // Log them out if they aren't an admin
                            auth.signOut()
                            Toast.makeText(context, "Access Denied: Not an Admin", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}