package com.example.hockeyapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeyapp.data.models.Player
import com.example.hockeyapp.data.repositories.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class PlayerViewModel(private val repository: PlayerRepository = PlayerRepository()) : ViewModel() {

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _teamPlayers = MutableStateFlow<List<Player>>(emptyList())
    val teamPlayers: StateFlow<List<Player>> = _teamPlayers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAllPlayers()
    }

    fun loadAllPlayers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getAllPlayers().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { _players.value = it },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    fun loadPlayersByTeam(teamId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getPlayersByTeam(teamId).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { _teamPlayers.value = it },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    fun registerPlayer(
        name: String,
        surname: String,
        dateOfBirth: Date,
        contactNumber: String,
        email: String,
        position: String,
        photoBytes: ByteArray?,
        teamId: String,
        jerseyNumber: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Upload photo if provided
                val photoUrl = if (photoBytes != null) {
                    repository.uploadPlayerPhoto(photoBytes).getOrThrow()
                } else {
                    ""
                }

                // Create player
                val player = Player(
                    name = name,
                    surname = surname,
                    dateOfBirth = dateOfBirth,
                    contactNumber = contactNumber,
                    email = email,
                    position = position,
                    photoUrl = photoUrl,
                    teamId = teamId,
                    jerseyNumber = jerseyNumber,
                    registeredAt = Date()
                )

                repository.registerPlayer(player).getOrThrow()
                loadAllPlayers()
                if (teamId.isNotEmpty()) {
                    loadPlayersByTeam(teamId)
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}