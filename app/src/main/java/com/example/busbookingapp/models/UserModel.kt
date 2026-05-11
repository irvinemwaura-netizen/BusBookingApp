package com.example.busbookingapp.models

data class UserModel(
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val userId: String = "",
    val name: String

)
// Example Model
data class AdminUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "admin"
)