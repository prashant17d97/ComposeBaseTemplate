package com.example.basetemplate.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basetemplate.repo.uistatus.LoadingType
import com.example.basetemplate.repo.uistatus.UIState
import com.example.basetemplate.repo.uistatus.UiStatusInfoOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    uiStatusInfoOwner: UiStatusInfoOwner
) : ViewModel() {

    val loadingType = uiStatusInfoOwner.uiState

}