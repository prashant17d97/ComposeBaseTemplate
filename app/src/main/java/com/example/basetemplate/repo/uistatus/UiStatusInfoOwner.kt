package com.example.basetemplate.repo.uistatus

import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import com.example.basetemplate.R
import kotlinx.coroutines.flow.StateFlow


/**
 * Interface for components that can manage and display UI status information using a [StateFlow].
 */
interface UiStatusInfoOwner {

    /**
     * A [StateFlow] representing the current UI state.
     */
    val uiState: StateFlow<UIState>

    /**
     * Function to set the UI state to a new [LoadingType].
     *
     * @param loadingType The new UI state to set.
     */
    fun setUIState(loadingType: UIState)

    fun loadRead(loadingType: UIState = UIState.LOADING())

}

/**
 * Sealed class representing various UI states, such as Loading, Error, Success, and Failure.
 */
sealed class UIState {

    /**
     * UI state indicating that the system is in a loading state.
     *
     * @param message The loading message to display (default is "Please wait!").
     * @param type The type of loading indicator (default is FullScreenLoader).
     */
    data class LOADING(
        val type: LoadingType = LoadingType.FullScreenLoader(message = "Please wait!")
    ) : UIState()

    /**
     * UI state indicating an error condition.
     *
     * @param type The type of error display (e.g., SnackBar).
     */
    data class Error(val type: LoadingType) : UIState()

    /**
     * UI state indicating a success condition.
     *
     * @param type The type of success display (e.g., SnackBar).
     */
    data class Success(val type: LoadingType) : UIState()

    /**
     * UI state indicating a failure condition.
     *
     * @param type The type of failure display (e.g., SnackBar).
     */
    data class Failure(val type: LoadingType) : UIState()

    val loadingType: LoadingType
        get() = when (val uiState = this) {
            is Error -> uiState.type
            is Failure -> uiState.type
            is LOADING -> uiState.type
            is Success -> uiState.type
        }
}

/**
 * Sealed class representing various types of loading indicators.
 */
sealed class LoadingType {
    /**
     * Loading indicator displayed as a SnackBar.
     *
     * @param message to display info.
     * @param actionLabel The label for the action button (default is "Proceed").
     * @param actionAllowed Flag indicating whether the action is allowed (default is true).
     * @param icon The resource ID of the icon to display.
     * @param duration The duration of the SnackBar (default is Short).
     * @param backgroundColor The background color of the SnackBar (default is R.color.success).
     */
    data class SnackBar(
        val message: String,
        val actionLabel: String = "Proceed",
        val actionAllowed: Boolean = true,
        @DrawableRes val icon: Int = R.drawable.ic_success,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        @ColorRes val backgroundColor: Int = R.color.success
    ) : LoadingType()

    /**
     * Loading indicator displayed as a Toast message.
     *
     * @param message to display info.
     * @param duration The duration of the Toast (default is Toast.LENGTH_SHORT).
     */
    data class Toasts(val message: String, val duration: Int = Toast.LENGTH_SHORT) : LoadingType()

    /**
     * Loading indicator displayed as a full-screen loader.
     *
     * @param message to display info.
     * @param actionLabel The label for the action button (default is "Proceed").
     * @param actionAllowed Flag indicating whether the action is allowed (default is false).
     * @param dismissOnBackPress Flag indicating whether the loader can be dismissed on back press (default is true).
     * @param dismissOnClickOutside Flag indicating whether the loader can be dismissed by clicking outside (default is true).
     * @param icon The resource ID of the icon to display.
     * @param duration The duration of the loader (default is Short).
     * @param backgroundColor The background color of the loader (default is R.color.warning).
     */
    data class FullScreenLoader(
        val message: String,
        val actionLabel: String = "Proceed",
        val actionAllowed: Boolean = false,
        val dismissOnBackPress: Boolean = true,
        val dismissOnClickOutside: Boolean = true,
        @DrawableRes val icon: Int = R.drawable.warning,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        @ColorRes val backgroundColor: Int = R.color.loading
    ) : LoadingType()
}
