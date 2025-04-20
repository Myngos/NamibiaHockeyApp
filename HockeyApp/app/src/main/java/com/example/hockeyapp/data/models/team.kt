package com.example.hockeyapp.data.models

import java.util.Date


data class Team(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val division: String = "",
    val logoUrl: String = "",
    val coachName: String = "",
    val coachContact: String = "",
    val teamColors: String = "",
    val createdAt: Date = Date(),
    val playerIds: List<String> = listOf()
)