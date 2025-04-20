package com.example.namibiahockeyapp.data.models


data class Event(
    val id: String = "",
    val name: String = "",
    val type: String = "", // tournament, match, training
    val startDate: Long = 0,
    val endDate: Long = 0,
    val location: String = "",
    val description: String = "",
    val participatingTeamIds: List<String> = listOf(),
    val status: String = "upcoming", // upcoming, ongoing, completed
    val createdBy: String = "",
    val entryFee: Double = 0.0
)