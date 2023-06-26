package com.example.assignit.model

data class Group(
    val id: String,
    val name: String,
    val admin: User, // New field for admin user
    val members: MutableList<User> = mutableListOf(admin),
)
