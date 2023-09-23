package com.example.basetemplate.repo

import com.example.basetemplate.repo.datamodel.Post
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    val postsResponse: StateFlow<List<Post>>
    val postResponse: StateFlow<Post>

    suspend fun getPost()

    suspend fun getPostById(id: Int)

    fun setPostsResponse(posts: List<Post>)
    fun setPostResponse(posts: Post)
}