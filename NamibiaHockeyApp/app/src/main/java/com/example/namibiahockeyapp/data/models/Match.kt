package com.example.namibiahockeyapp.data.models

data class Match(
    val id: String = "",
    val eventId: String = "",
    val teamAId: String = "",
    val teamBId: String = "",
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val startTime: Long = 0,
    val status: String = "scheduled", // scheduled, in_progress, completed
    val location: String = "",
    val highlights: List<String> = listOf()
)