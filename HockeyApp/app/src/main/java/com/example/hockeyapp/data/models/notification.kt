package com.example.hockeyapp.data.models

data class Notification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "", // match_update, news, announcement
    val relatedId: String = "", // team ID, match ID, etc.
    val timestamp: Long = System.currentTimeMillis(),
    val recipientIds: List<String> = listOf()
)