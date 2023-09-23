package com.example.basetemplate.main

import androidx.lifecycle.ViewModel
import com.example.basetemplate.repo.uistatus.CurrentStateIndicator
import com.example.basetemplate.repo.uistatus.UiStatusInfoOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val uiStatusInfoOwner: UiStatusInfoOwner
) : ViewModel() {

    val currentLoadingStateIndicator = uiStatusInfoOwner.currentUILoadingState

    fun updateLoadingState(currentStateIndicator: CurrentStateIndicator) {
        uiStatusInfoOwner.setUIState(currentStateIndicator = currentStateIndicator)
    }

    fun setNetWorkAvailability(isAvailable: Boolean) {
        uiStatusInfoOwner.setNetWorkAvailability(isAvailable = isAvailable)
    }


}