package com.example.basetemplate.repo

import com.example.basetemplate.network.APIsEndPoints
import com.example.basetemplate.network.Status
import com.example.basetemplate.network.extentions.mapToResource
import com.example.basetemplate.repo.datamodel.Post
import com.example.basetemplate.repo.uistatus.COLOR
import com.example.basetemplate.repo.uistatus.CurrentStateIndicator
import com.example.basetemplate.repo.uistatus.Icon
import com.example.basetemplate.repo.uistatus.UiStatusInfoOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apIsEndPoints: APIsEndPoints,
    private val uiStatusInfoOwner: UiStatusInfoOwner
) : Repository {


    private val _postsResponse: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    override val postsResponse: StateFlow<List<Post>>
        get() = _postsResponse

    private val _postResponse: MutableStateFlow<Post> = MutableStateFlow(Post("", 0, "", 0))

    override val postResponse: StateFlow<Post>
        get() = _postResponse


    override suspend fun getPost() {
        activeNetWorkCall {
            try {
                apIsEndPoints.getPost().mapToResource().also { postResponse ->
                    val uiState = when (postResponse.status) {
                        Status.SUCCESS -> {
                            postResponse.data?.let { setPostsResponse(it) }
                            CurrentStateIndicator.SnackBar(
                                message = postResponse.message.takeIf { it?.isNotBlank() == true }
                                    ?: "Successfully loaded.",
                                actionAllowed = false,
                                icon = Icon.SUCCESS,
                                backgroundColor = COLOR.SUCCESS
                            )
                        }

                        Status.ERROR -> CurrentStateIndicator.SnackBar(
                            message = postResponse.message ?: "Some error occurred!",
                            actionAllowed = false,
                            icon = Icon.ERROR,
                            backgroundColor = COLOR.ERROR
                        )

                        Status.LOADING -> CurrentStateIndicator.FullScreenLoader(
                            message = "Please wait! Loading....",
                            actionAllowed = false,
                            dismissOnClickOutside = false,
                            dismissOnBackPress = false
                        )

                        Status.FAILURE -> CurrentStateIndicator.SnackBar(
                            message = postResponse.message ?: "Something went wrong!",
                        )
                    }

                    uiStatusInfoOwner.setUIState(uiState)
                }


            } catch (e: Exception) {
                // Handle the exception here (e.g., show an error message or log the error)
                uiStatusInfoOwner.setUIState(
                    CurrentStateIndicator.SnackBar(
                        message = "Something went wrong!${e.localizedMessage}",
                        actionAllowed = false,
                        icon = Icon.ERROR,
                        backgroundColor = COLOR.ERROR
                    )
                )
            }

        }
    }

    override suspend fun getPostById(id: Int) {
        activeNetWorkCall {
            apIsEndPoints.getPostById(id).mapToResource().also { postResponse ->
                val uiState = when (postResponse.status) {
                    Status.SUCCESS -> {
                        postResponse.data?.let { setPostResponse(it) }
                        CurrentStateIndicator.SnackBar(message = postResponse.message.takeIf { it?.isNotBlank() == true }
                            ?: "Successfully loaded.",
                            actionAllowed = false,
                            icon = Icon.SUCCESS,
                            backgroundColor = COLOR.SUCCESS)
                    }

                    Status.ERROR -> CurrentStateIndicator.SnackBar(
                        message = postResponse.message ?: "Some error occurred!",
                        actionAllowed = false,
                        icon = Icon.ERROR,
                        backgroundColor = COLOR.ERROR
                    )

                    Status.LOADING -> CurrentStateIndicator.FullScreenLoader(
                        message = "Please wait! Loading....",
                        actionAllowed = false,
                        dismissOnClickOutside = false,
                        dismissOnBackPress = false
                    )

                    Status.FAILURE -> CurrentStateIndicator.SnackBar(
                        message = postResponse.message ?: "Something went wrong!",
                    )
                }
                uiStatusInfoOwner.setUIState(uiState)

            }
        }

    }

    override fun setPostsResponse(posts: List<Post>) {
        _postsResponse.tryEmit(posts)
    }

    override fun setPostResponse(posts: Post) {
        _postResponse.tryEmit(posts)
    }

    private suspend fun activeNetWorkCall(active: suspend CoroutineScope.() -> Unit) {
        uiStatusInfoOwner.isNetWorkAvailable.collect {
            if (it == true) {
                uiStatusInfoOwner.loadRead()
                coroutineScope { // Create a CoroutineScope
                    active() // Call the active function within the scope
                }
            } else {
                uiStatusInfoOwner.setUIState(
                    CurrentStateIndicator.SnackBar(
                        message = "You're Offline!",
                        actionAllowed = true,
                        actionLabel = "Turn on",
                        icon = Icon.WARNING,
                        backgroundColor = COLOR.WARNING
                    )
                )

            }
        }

    }
}

