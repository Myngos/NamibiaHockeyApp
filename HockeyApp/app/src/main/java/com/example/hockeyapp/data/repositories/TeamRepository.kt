package com.example.hockeyapp.data.repositories

import com.example.hockeyapp.data.models.Team
import com.example.hockeyapp.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TeamRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    suspend fun registerTeam(team: Team): Result<String> {
        return try {
            val teamId = firebaseService.registerTeam(team)
            Result.success(teamId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllTeams(): Flow<Result<List<Team>>> = flow {
        try {
            val teams = firebaseService.getAllTeams()
            emit(Result.success(teams))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getTeamById(teamId: String): Result<Team?> {
        return try {
            val team = firebaseService.getTeamById(teamId)
            Result.success(team)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadTeamLogo(imageBytes: ByteArray): Result<String> {
        return try {
            val imageUrl = firebaseService.uploadImage(imageBytes, "team_logos")
            Result.success(imageUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
