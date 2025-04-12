package com.example.pix.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pix.ui.viewmodel.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pix.ui.views.PhotoDetailScreen
import com.example.pix.ui.views.PhotosScreen

// Test Assignment by Tazmin Vadim
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            val viewModel: PhotosViewModel = hiltViewModel()
            val photos by viewModel.photos.collectAsState()
            val isLoading by viewModel.isLoading.collectAsState()
            val error by viewModel.error.collectAsState()
            val lastQuery by viewModel.lastQuery.collectAsState()
            val mediumQuality = "c" //800px
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "photos") {

                composable("photos"){
                    PhotosScreen(
                        photos = photos,
                        isLoading = isLoading,
                        error = error,
                        onReload = { viewModel.loadPhotos(lastQuery, 1, 100, mediumQuality)},
                        onSearch = { query -> viewModel.loadPhotos(query, 1, 100, mediumQuality)},
                        onPhotoCLick = { picture ->
                            navController.navigate("photoDetail/${Uri.encode(picture.url)}/${Uri.encode(picture.title)}")}
                    )
                }

                composable("photoDetail/{photoUrl}/{title}") { backStackEntry ->
                    val photoUrl = backStackEntry.arguments?.getString("photoUrl")
                    val title = backStackEntry.arguments?.getString("title")
                    if (photoUrl != null && title != null) {
                        PhotoDetailScreen(url = photoUrl, title = title, onBack = { navController.popBackStack()})
                    }
                }
            }
        }
    }
}


