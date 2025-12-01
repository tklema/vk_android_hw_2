package com.example.homework_2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework_2.client.RetrofitDog
import com.example.homework_2.data.DogImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DogViewModel() : ViewModel() {
    private val _dogImages = MutableStateFlow<List<DogImage>>(emptyList())
    val dogImages: StateFlow<List<DogImage>> = _dogImages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var canLoadMore = true
    private var isLoadingInProgress = false

    init {
        loadDogs()
    }

    fun loadDogs() {
        if (isLoadingInProgress) return

        viewModelScope.launch {
            isLoadingInProgress = true
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitDog.dogClient.getDog()

                if (response.isNotEmpty()) {
                    _dogImages.value = response
                    canLoadMore = true
                } else {
                    _error.value = "Пустой ответ от API"
                    canLoadMore = false
                }
            } catch (e: Exception) {
                _error.value = "Ошибка: ${e.message}"
                canLoadMore = false
            } finally {
                _isLoading.value = false
                isLoadingInProgress = false
            }
        }
    }

    fun loadMoreDogs() {
        if (isLoadingInProgress || !canLoadMore) return

        viewModelScope.launch {
            isLoadingInProgress = true
            _isLoadingMore.value = true
            try {
                val response = RetrofitDog.dogClient.getDog()
                if (response.isNotEmpty()) {
                    val currentList = _dogImages.value.toMutableList()
                    currentList.addAll(response)
                    _dogImages.value = currentList
                } else {
                    canLoadMore = false
                }
            } catch (e: Exception) {
                _error.value = "Ошибка при загрузке: ${e.message}"
                canLoadMore = false
            } finally {
                _isLoadingMore.value = false
                isLoadingInProgress = false
            }
        }
    }

    fun createNotification(context: Context, imageIndex: Int) {
        android.widget.Toast.makeText(
            context,
            "Собака #${imageIndex + 1}",
            android.widget.Toast.LENGTH_LONG
        ).show()
    }
}