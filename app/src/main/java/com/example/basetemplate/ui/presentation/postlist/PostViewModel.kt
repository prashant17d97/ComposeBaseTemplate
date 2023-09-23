package com.example.basetemplate.ui.presentation.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basetemplate.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class responsible for managing post data.
 *
 * @param repository The repository for fetching post data.
 */
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    /**
     * A [Flow] of posts retrieved from the repository.
     */
    val postsResponse = repository.postsResponse
    val postResponse = repository.postResponse

    /**
     * Asynchronously fetches and updates the list of posts.
     */

    init {
        getPost()
    }

    fun getPost() {
        viewModelScope.launch {
            repository.getPost()
        }
    }

    fun getPostById(id: Int) {
        viewModelScope.launch {
            repository.getPostById(id)
        }
    }
}
