package com.example.hockeyapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeyapp.data.models.Event
import com.example.hockeyapp.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class EventViewModel(private val repository: EventRepository = EventRepository()) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getAllEvents().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { _events.value = it },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    fun createEvent(
        name: String,
        description: String,
        startDate: Date,
        endDate: Date,
        location: String,
        eventType: String,
        participatingTeams: List<String>,
        imageBytes: ByteArray?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Upload image if provided
                val imageUrl = if (imageBytes != null) {
                    repository.uploadEventImage(imageBytes).getOrThrow()
                } else {
                    ""
                }

                // Create event
                val event = Event(
                    name = name,
                    description = description,
                    startDate = startDate,
                    endDate = endDate,
                    location = location,
                    eventType = eventType,
                    participatingTeams = participatingTeams,
                    createdAt = Date(),
                    imageUrl = imageUrl
                )

                repository.createEvent(event).getOrThrow()
                loadEvents()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}