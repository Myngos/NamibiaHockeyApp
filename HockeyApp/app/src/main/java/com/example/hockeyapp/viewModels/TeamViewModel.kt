package com.example.hockeyapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeyapp.data.models.Team
import com.example.hockeyapp.data.repositories.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TeamViewModel(private val repository: TeamRepository = TeamRepository()) : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadTeams()
    }

    fun loadTeams() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getAllTeams().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { _teams.value = it },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    fun registerTeam(
        name: String,
        division: String,
        logoBytes: ByteArray?,
        coachName: String,
        coachContact: String,
        teamColors: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Upload logo if provided
                val logoUrl = if (logoBytes != null) {
                    repository.uploadTeamLogo(logoBytes).getOrThrow()
                } else {
                    ""
                }

                // Create team
                val team = Team(
                    name = name,
                    division = division,
                    logoUrl = logoUrl,
                    coachName = coachName,
                    coachContact = coachContact,
                    teamColors = teamColors,
                    createdAt = Date()
                )

                repository.registerTeam(team).getOrThrow()
                loadTeams()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
