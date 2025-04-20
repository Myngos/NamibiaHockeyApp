package com.example.hockeyapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeyapp.data.models.News
import com.example.hockeyapp.data.repositories.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class NewsViewModel(private val repository: NewsRepository = NewsRepository()) : ViewModel() {

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getAllNews().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { _news.value = it },
                    onFailure = { _error.value = it.message }
                )
            }
        }
    }

    fun publishNews(
        title: String,
        content: String,
        imageBytes: ByteArray?,
        authorId: String,
        authorName: String,
        relatedEventId: String? = null,
        relatedTeamId: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Upload image if provided
                val imageUrl = if (imageBytes != null) {
                    repository.uploadNewsImage(imageBytes).getOrThrow()
                } else {
                    ""
                }

                // Create news
                val news = News(
                    title = title,
                    content = content,
                    imageUrl = imageUrl,
                    authorId = authorId,
                    authorName = authorName,
                    publishedDate = Date(),
                    relatedEventId = relatedEventId,
                    relatedTeamId = relatedTeamId
                )

                repository.publishNews(news).getOrThrow()
                loadNews()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}