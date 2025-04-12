package com.example.pix.di

import android.content.Context
import androidx.room.Room
import com.example.pix.data.flickr.FlickrApi
import com.example.pix.data.flickr.FlickrRepositoryImpl
import com.example.pix.data.room.PictureDao
import com.example.pix.data.room.PictureDatabase
import com.example.pix.domain.repository.FlickrRepository
import com.example.pix.domain.usecase.GetPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFlickrApi(): FlickrApi{
        return Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickrApi::class.java)
    }

    @Provides
    fun providePictureDao(db: PictureDatabase): PictureDao {
        return db.getPictureDao()
    }

    @Provides
    fun providePictureDatabase(@ApplicationContext context: Context): PictureDatabase {
        return Room.databaseBuilder(
            context,
            PictureDatabase::class.java,
            "pictures.db"
        ).build()
    }

    @Provides
    fun provideFlickrRepository(
        flickrApi: FlickrApi,
        pictureDao: PictureDao
    ): FlickrRepository {
        return FlickrRepositoryImpl(flickrApi, pictureDao)
    }

    @Provides
    fun provideGetPhotosUseCase(flickrRepository: FlickrRepository): GetPhotosUseCase {
        return GetPhotosUseCase(flickrRepository)
    }
}