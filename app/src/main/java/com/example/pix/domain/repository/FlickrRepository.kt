package com.example.pix.domain.repository

import com.example.pix.domain.entity.Picture

interface FlickrRepository {
    suspend fun searchPhotos(text: String, page: Int, count: Int, quality: String): Result<List<Picture>>
}