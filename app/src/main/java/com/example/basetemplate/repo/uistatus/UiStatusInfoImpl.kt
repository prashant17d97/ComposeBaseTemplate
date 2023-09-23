package com.example.basetemplate.repo.uistatus

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UiStatusInfoImpl : UiStatusInfoOwner {
    private val _uiState: MutableStateFlow<CurrentStateIndicator> =
        MutableStateFlow(CurrentStateIndicator.IDLE)
    override val currentUILoadingState: StateFlow<CurrentStateIndicator>
        get() = _uiState

    private val _isNetWorkAvailable: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    override val isNetWorkAvailable: StateFlow<Boolean?>
        get() = _isNetWorkAvailable

    override fun setUIState(currentStateIndicator: CurrentStateIndicator) {
        _uiState.tryEmit(currentStateIndicator)
    }

    override fun setNetWorkAvailability(isAvailable: Boolean?) {
        _isNetWorkAvailable.tryEmit(isAvailable)
    }

    override fun loadRead(currentStateIndicator: CurrentStateIndicator) {
        _uiState.tryEmit(currentStateIndicator)
    }


}