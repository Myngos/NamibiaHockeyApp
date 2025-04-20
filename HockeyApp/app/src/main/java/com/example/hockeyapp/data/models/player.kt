package com.example.hockeyapp.data.models

import java.util.Date

data class Player(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val dateOfBirth: Date = Date(),
    val contactNumber: String = "",
    val email: String = "",
    val position: String = "",
    val photoUrl: String = "",
    val teamId: String = "",
    val jerseyNumber: Int = 0,
    val registeredAt: Date = Date()
)