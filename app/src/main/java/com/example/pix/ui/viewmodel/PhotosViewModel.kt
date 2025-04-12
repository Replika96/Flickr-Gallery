package com.example.pix.ui.viewmodel

import com.example.pix.domain.entity.Picture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pix.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase
): ViewModel() {
    private val _photos = MutableStateFlow<List<Picture>>(emptyList())
    val photos: StateFlow<List<Picture>> = _photos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _lastQuery = MutableStateFlow("cats") //можно было бы оставить, чтобы котики были по умолчанию при перезагрузки, но попробую так
    val lastQuery: StateFlow<String> = _lastQuery.asStateFlow()

    init {
        loadPhotos("cats", 1, 100, "c") //указываем  кол-во картинок начальное и качество
    }

    // загружаем котиков c flickr
    fun loadPhotos(text: String, page: Int, count: Int, quality: String) {
        viewModelScope.launch{
            _isLoading.value = true
            _lastQuery.value = text
            try{
                val result = getPhotosUseCase(text, page, count, quality)
                result.onSuccess { pictures ->
                    _photos.value = pictures
                    _error.value = null
                }.onFailure { throwable ->
                    _error.value = "Ошибка: ${throwable.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}