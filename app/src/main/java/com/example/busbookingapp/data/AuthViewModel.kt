package com.example.busbookingapp.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.busbookingapp.models.UserModel
import com.example.busbookingapp.navigation.Routes.ROUTE_DASHBOARD
import com.example.busbookingapp.navigation.Routes.ROUTE_LOGIN
import com.example.busbookingapp.navigation.Routes.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signup(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        navController: NavController,
        context: Context,
        phoneNumber: String
    ) {
        Log.d("AUTH", "signup() called with email: $email")

        if (username.isBlank() || email.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            Log.d("AUTH", "❌ Blank field detected")
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
            Log.d("AUTH", "❌ Passwords don't match")
            return
        }

        Log.d("AUTH", "Calling createUserWithEmailAndPassword...")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AUTH", "✅ Auth user created: ${auth.currentUser?.uid}")
                    val userId = auth.currentUser?.uid ?: ""
                    val user = UserModel(
                        username = username,
                        phoneNumber = phoneNumber,
                        email = email,
                        userId = userId
                    )
                    saveUserToDatabase(user, navController, context)
                } else {
                    Log.e("AUTH", "❌ Auth failed: ${task.exception?.message}")
                    Toast.makeText(
                        context,
                        task.exception?.message ?: "Registration failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun saveUserToDatabase(
        user: UserModel,
        navController: NavController,
        context: Context
    ) {
        Log.d("AUTH", "saveUserToDatabase() called for userId: ${user.userId}")

        // ✅ Add the explicit database URL
        val dbRef = FirebaseDatabase
            .getInstance("https://busbookingapp-b6733-default-rtdb.firebaseio.com")
            .getReference("User/${user.userId}")

        dbRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("AUTH", "✅ DB save successful — navigating to dashboard")
                Toast.makeText(context, "User Registered successfully", Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_DASHBOARD) {
                    popUpTo(ROUTE_REGISTER) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                Log.e("AUTH", "❌ DB save failed: ${task.exception?.message}")
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Failed to save user",
                    Toast.LENGTH_LONG
                ).show()
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
}