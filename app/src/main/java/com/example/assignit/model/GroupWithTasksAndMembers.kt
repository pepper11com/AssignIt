package com.example.assignit.model

import java.util.Date

data class GroupWithTasksAndMembers(
    val id: String,
    val name: String,
    val admin: User,
    val members: List<User>,
    val dayAndTimeEdited: Date,
    val tasks: List<Task>,
)