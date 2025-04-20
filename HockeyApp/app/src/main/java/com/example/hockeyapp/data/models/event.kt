package com.example.hockeyapp.data.models

import java.util.Date

data class Event(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val location: String = "",
    val eventType: String = "", // Tournament, Match, Training, etc.
    val participatingTeams: List<String> = listOf(),
    val createdAt: Date = Date(),
    val imageUrl: String = ""
)