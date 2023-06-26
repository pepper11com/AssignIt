package com.example.assignit.model

data class Invitation(
    val groupId: String,
    val inviterId: String,
    val inviteeId: String,
    val status: String = "pending" // to track whether the invitation has been accepted, rejected, or is pending
)
