package com.example.hockeyapp.data.models

import java.util.Date

data class News(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val publishedDate: Date = Date(),
    val relatedEventId: String? = null,
    val relatedTeamId: String? = null
)