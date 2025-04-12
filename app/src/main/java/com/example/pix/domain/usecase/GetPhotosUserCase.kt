package com.example.pix.domain.usecase

import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repository.FlickrRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val flickrRepository: FlickrRepository
) {
    suspend operator fun invoke(text: String, page: Int, count: Int, quality:String): Result<List<Picture>>{
        if (text.isBlank()) {
            return Result.failure(IllegalArgumentException("Заполните текст"))
        }
        return try {
            val result = flickrRepository.searchPhotos(text, page, count, quality)
            result.map { pictures ->
                pictures.filter { it.url.isNotEmpty() }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}