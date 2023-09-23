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
    val currentUILoadingState: StateFlow<CurrentStateIndicator>
    val isNetWorkAvailable: StateFlow<Boolean?>

    /**
     * Function to set the UI state to a new [CurrentStateIndicator].
     *
     * @param currentStateIndicator The new UI state to set.
     */
    fun setUIState(currentStateIndicator: CurrentStateIndicator)
    fun setNetWorkAvailability(isAvailable: Boolean?)

    fun loadRead(
        currentStateIndicator: CurrentStateIndicator = CurrentStateIndicator.FullScreenLoader(
            "Please wait! while loading..."
        )
    )

}


/**
 * Sealed class representing various types of loading indicators.
 */
sealed class CurrentStateIndicator {
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
        val icon: Icon = Icon.SUCCESS,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val backgroundColor: COLOR = COLOR.SUCCESS
    ) : CurrentStateIndicator()

    /**
     * Loading indicator displayed as a Toast message.
     *
     * @param message to display info.
     * @param duration The duration of the Toast (default is Toast.LENGTH_SHORT).
     */
    data class Toasts(val message: String, val duration: Int = Toast.LENGTH_SHORT) :
        CurrentStateIndicator()

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
        val icon: Icon = Icon.Loading,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val backgroundColor: COLOR = COLOR.Loading
    ) : CurrentStateIndicator()

    data object IDLE : CurrentStateIndicator()
}

enum class COLOR(@ColorRes val color: Int) {
    Loading(R.color.loading),
    WARNING(R.color.warning),
    SUCCESS(R.color.success),
    INFO(R.color.success),
    ERROR(R.color.error),
}

enum class Icon(@DrawableRes val icon: Int) {
    Loading(R.drawable.ic_loading),
    WARNING(R.drawable.warning),
    SUCCESS(R.drawable.ic_success),
    INFO(R.drawable.ic_ligh_bulb),
    ERROR(R.drawable.ic_error),
}
