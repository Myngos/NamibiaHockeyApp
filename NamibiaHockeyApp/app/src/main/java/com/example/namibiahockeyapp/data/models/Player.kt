package com.example.namibiahockeyapp.data.models

data class Player(
    val id: String = "",
    val userId: String = "",
    val teamId: String = "",
    val position: String = "",
    val jerseyNumber: Int = 0,
    val dateOfBirth: Long = 0,
    val height: Int = 0, // cm
    val weight: Int = 0, // kg
    val emergencyContact: String = "",
    val medicalInfo: String = "",
    val stats: Map<String, Int> = mapOf() // goals, assists, etc.
)