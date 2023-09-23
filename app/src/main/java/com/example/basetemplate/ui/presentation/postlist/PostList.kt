package com.example.basetemplate.ui.presentation.postlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.basetemplate.repo.datamodel.Post
import com.example.basetemplate.ui.navigation.Screens


/**
 * Composable function for displaying a list of posts.
 *
 * @param navHostController The navigation controller for navigating to other screens.
 * @param postViewModel The view model responsible for managing post data.
 */
@Composable
fun PostList(
    navHostController: NavHostController,
    postViewModel: PostViewModel = hiltViewModel(),
) {

    val posts by postViewModel.postsResponse.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(
            space = 5.dp,
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            items(items = posts) { post ->
                PostCard(post = post) {
                    navHostController.navigate(Screens.Post.postIdArgs(post.id))
                }
            }
        })
}

/**
 * Composable function for displaying a single post card.
 *
 * @param post The post data to display.
 * @param onPostClick The callback function to invoke when the post card is clicked.
 */
@Composable
fun PostCard(post: Post, onPostClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 7.5.dp)
            .clickable {
                onPostClick.invoke()
            }, shape = RoundedCornerShape(10.dp), elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Title: ${post.title}")
            Text(text = "User ID: ${post.userId}")
            Text(text = "ID: ${post.id}")
            Text(text = "Body:\n${post.body}")
        }
    }
}

/**
 * Composable function for previewing the PostCard composable.
 */
@Preview
@Composable
fun PostListPreview() {
    PostCard(
        post = Post(
            body = "A quick brown fox jumps over the lazy dog",
            id = 0,
            userId = 1,
            title = "Prashant Singh"
        )
    ) {}
}
