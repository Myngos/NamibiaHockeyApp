package com.example.hockeyapp.data.models


data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val role: String = "player", // player, coach, manager, admin
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)