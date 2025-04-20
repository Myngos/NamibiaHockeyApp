package com.example.hockeyapp.data.repositories

import com.example.hockeyapp.data.models.Player
import com.example.hockeyapp.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    suspend fun registerPlayer(player: Player): Result<String> {
        return try {
            val playerId = firebaseService.registerPlayer(player)
            Result.success(playerId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPlayersByTeam(teamId: String): Flow<Result<List<Player>>> = flow {
        try {
            val players = firebaseService.getPlayersByTeam(teamId)
            emit(Result.success(players))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getAllPlayers(): Flow<Result<List<Player>>> = flow {
        try {
            val players = firebaseService.getAllPlayers()
            emit(Result.success(players))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadPlayerPhoto(imageBytes: ByteArray): Result<String> {
        return try {
            val imageUrl = firebaseService.uploadImage(imageBytes, "player_photos")
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}