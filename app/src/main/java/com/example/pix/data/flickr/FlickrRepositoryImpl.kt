package com.example.pix.data.flickr

import com.example.pix.data.flickr.mapper.toEntity
import com.example.pix.data.room.PictureDao
import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repository.FlickrRepository
import java.io.IOException
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor(
    private val flickrApi: FlickrApi,
    private val pictureDao: PictureDao //кешировать в бд?
): FlickrRepository {

    override suspend fun searchPhotos(text: String, page: Int, count: Int, quality: String): Result<List<Picture>> = runCatching {
        val result = flickrApi.search(text, page, count)
        result.photos?.photo?.map { it.toEntity(quality) } ?: throw IOException("Фото не найдены")
    }
}

/* можно было бы добавить наверно кэширование в Room:
Сначала проверить, есть ли что-то в базе
    val cachedPhotos = pictureDao.getAll()
    if (cachedPhotos.isNotEmpty()) {
        return@runCatching cachedPhotos.map { it.toDomain() например } // Вернуть из кэша, если есть
    }
Если кэша нет, сделать запрос к API
    val result = flickrApi.search(text, page, count)
    val photos = result.photos?.photo?.map { it.toEntity(quality) } ?: throw IOException("Фото не найдены")
Сохранить в базу
    pictureDao.insertAll(photos)
Вернуть результат
photos.map { it.toDomain()  } */