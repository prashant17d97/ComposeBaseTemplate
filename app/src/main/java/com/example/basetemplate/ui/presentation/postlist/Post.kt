package com.example.basetemplate.ui.presentation.postlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Post(
    postViewModel: PostViewModel = hiltViewModel(),
    id: Int?
) {

    val post by postViewModel.postResponse.collectAsState()
    LaunchedEffect(key1 = Unit, block = {
        if (id != null) {
            postViewModel.getPostById(id)
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostCard(post = post) {

        }
    }
}