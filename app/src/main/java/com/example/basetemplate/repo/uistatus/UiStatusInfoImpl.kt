package com.example.basetemplate.repo.uistatus

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UiStatusInfoImpl : UiStatusInfoOwner {
    private val _uiState: MutableStateFlow<UIState> =
        MutableStateFlow(UIState.LOADING())
    override val uiState: StateFlow<UIState>
        get() = _uiState

    override fun setUIState(loadingType: UIState) {
        _uiState.tryEmit(loadingType)
    }

    override fun loadRead(loadingType: UIState) {
        _uiState.tryEmit(loadingType)
    }


}