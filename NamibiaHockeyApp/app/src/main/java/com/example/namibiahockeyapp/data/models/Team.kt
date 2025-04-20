package com.example.namibiahockeyapp.data.models


data class Team(
    val id: String = "",
    val name: String = "",
    val category: String = "", // men, women, junior, etc.
    val logoUrl: String = "",
    val colors: List<String> = listOf(),
    val managerId: String = "",
    val coachId: String = "",
    val playerIds: List<String> = listOf(),
    val createdAt: Long = System.currentTimeMillis()
)