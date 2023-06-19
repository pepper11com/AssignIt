package com.example.assignit.model

data class Group(
    val id: String,
    val name: String,
    val users: List<User>,
)
