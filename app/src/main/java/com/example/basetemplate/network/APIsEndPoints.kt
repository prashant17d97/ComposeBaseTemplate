package com.example.basetemplate.network

import com.example.basetemplate.network.Endpoints.GET_POST
import com.example.basetemplate.network.Endpoints.GET_POST_BY_ID
import com.example.basetemplate.repo.datamodel.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * An interface defining API endpoints for making network requests.
 */
interface APIsEndPoints {
    /**
     * Makes a GET request to retrieve a list of posts.
     *
     * @return A Retrofit [Response] containing a list of [Post] objects.
     */
    @GET(GET_POST)
    suspend fun getPost(): Response<List<Post>>

    @GET(GET_POST_BY_ID)
    suspend fun getPostById(@Path("postId") postId: Int): Response<Post>
}
