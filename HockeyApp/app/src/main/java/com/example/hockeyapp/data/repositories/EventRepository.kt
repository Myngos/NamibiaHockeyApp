package com.example.hockeyapp.data.repositories

import com.example.hockeyapp.data.models.Event
import com.example.hockeyapp.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    suspend fun createEvent(event: Event): Result<String> {
        return try {
            val eventId = firebaseService.createEvent(event)
            Result.success(eventId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllEvents(): Flow<Result<List<Event>>> = flow {
        try {
            val events = firebaseService.getAllEvents()
            emit(Result.success(events))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadEventImage(imageBytes: ByteArray): Result<String> {
        return try {
            val imageUrl = firebaseService.uploadImage(imageBytes, "event_images")
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}