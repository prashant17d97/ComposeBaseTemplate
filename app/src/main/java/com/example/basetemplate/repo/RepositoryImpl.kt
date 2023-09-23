package com.example.basetemplate.repo

import com.example.basetemplate.R
import com.example.basetemplate.network.APIsEndPoints
import com.example.basetemplate.network.Status
import com.example.basetemplate.network.extentions.mapToResource
import com.example.basetemplate.repo.datamodel.Post
import com.example.basetemplate.repo.uistatus.LoadingType
import com.example.basetemplate.repo.uistatus.UIState
import com.example.basetemplate.repo.uistatus.UiStatusInfoOwner
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
        apIsEndPoints.getPost().mapToResource().also { postResponse ->
            val uiState = when (postResponse.status) {
                Status.SUCCESS -> {
                    postResponse.data?.let { setPostsResponse(it) }
                    UIState.Success(
                        type = LoadingType.SnackBar(message = postResponse.message.takeIf { it?.isNotBlank() == true }
                            ?: "Successfully loaded.",
                            actionAllowed = false,
                            icon = R.drawable.ic_success,
                            backgroundColor = R.color.success))
                }

                Status.ERROR -> UIState.Error(
                    type = LoadingType.SnackBar(
                        message = postResponse.message ?: "Some error occurred!",
                        actionAllowed = false,
                        icon = R.drawable.ic_error,
                        backgroundColor = R.color.error
                    )
                )

                Status.LOADING -> UIState.LOADING(
                    type = LoadingType.FullScreenLoader(
                        message = "Please wait! Loading....",
                        actionAllowed = false,
                        dismissOnClickOutside = false,
                        dismissOnBackPress = false
                    )
                )

                Status.FAILURE -> UIState.Failure(
                    type = LoadingType.SnackBar(
                        message = postResponse.message ?: "Something went wrong!",
                    )
                )
            }
            uiStatusInfoOwner.setUIState(uiState)

        }

    }

    override suspend fun getPostById(id: Int) {
        uiStatusInfoOwner.loadRead()
        apIsEndPoints.getPostById(id).mapToResource().also { postResponse ->
            val uiState = when (postResponse.status) {
                Status.SUCCESS -> {
                    postResponse.data?.let { setPostResponse(it) }
                    UIState.Success(
                        type = LoadingType.SnackBar(message = postResponse.message.takeIf { it?.isNotBlank() == true }
                            ?: "Successfully loaded.",
                            actionAllowed = false,
                            icon = R.drawable.ic_success,
                            backgroundColor = R.color.success))
                }

                Status.ERROR -> UIState.Error(
                    type = LoadingType.SnackBar(
                        message = postResponse.message ?: "Some error occurred!",
                        actionAllowed = false,
                        icon = R.drawable.ic_error,
                        backgroundColor = R.color.error
                    )
                )

                Status.LOADING -> UIState.LOADING(
                    type = LoadingType.FullScreenLoader(
                        message = "Please wait! Loading....",
                        actionAllowed = false,
                        dismissOnClickOutside = false,
                        dismissOnBackPress = false
                    )
                )

                Status.FAILURE -> UIState.Failure(
                    type = LoadingType.SnackBar(
                        message = postResponse.message ?: "Something went wrong!",
                    )
                )
            }
            uiStatusInfoOwner.setUIState(uiState)

        }
    }

    override fun setPostsResponse(posts: List<Post>) {
        _postsResponse.tryEmit(posts)
    }

    override fun setPostResponse(posts: Post) {
        _postResponse.tryEmit(posts)
    }

}

