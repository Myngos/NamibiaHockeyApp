package com.example.hockeyapp.data.repositories

import com.example.hockeyapp.data.models.Event
import com.example.hockeyapp.data.models.News
import com.example.hockeyapp.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    suspend fun publishNews(news: News): Result<String> {
        return try {
            val newsId = firebaseService.publishNews(news)
            Result.success(newsId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllNews(): Flow<Result<List<News>>> = flow {
        try {
            val newsList = firebaseService.getAllNews()
            emit(Result.success(newsList))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadNewsImage(imageBytes: ByteArray): Result<String> {
        return try {
            val imageUrl = firebaseService.uploadImage(imageBytes, "news_images")
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}