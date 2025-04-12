package com.example.pix.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pix.R
import com.example.pix.domain.entity.Picture

@Composable
fun PhotosScreen(
    photos: List<Picture>,
    isLoading: Boolean,
    error: String?,
    onReload: () -> Unit,
    onSearch: (String) -> Unit,
    onPhotoCLick: (Picture) -> Unit
) {
    var searchText by remember { mutableStateOf("cats") }
    Scaffold(
        topBar = {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Поиск") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                trailingIcon = {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Очистить")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) })
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    error != null -> {
                        ErrorState(error = error, onReload = onReload)
                    }
                    else -> {
                        PhotosGrid(photos = photos, onPhotoCLick)
                    }
                }
            }
        }
    )
}

@Composable
fun PhotosGrid(
    photos: List<Picture>,
    onPhotoCLick: (Picture) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)) {
        items(photos){ picture->
            PictureItem(picture) { onPhotoCLick(picture) }
        }
    }
}

@Composable
fun PictureItem(
    picture: Picture,
    onPhotoClick: () -> Unit) {
    Box(modifier = Modifier.padding(4.dp)
        .aspectRatio(1f) //квадрат
        .clickable(onClick = onPhotoClick )){
        AsyncImage(model = picture.url,
            contentDescription = picture.title,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.ic_error_image))
    }
}

@Composable
fun ErrorState(error: String, onReload: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(error)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onReload) {
            Text(text="Повторить")
        }
    }
}
