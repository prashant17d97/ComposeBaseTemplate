package com.example.basetemplate.ui.presentation

import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.basetemplate.R
import com.example.basetemplate.repo.uistatus.CurrentStateIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Composable function for displaying UI loading state information.
 *
 * @param currentStateIndicator The type of loading indicator to use.
 */
@Composable
fun UiLoadingStateInfo(currentStateIndicator: CurrentStateIndicator) {
    when (currentStateIndicator) {
        is CurrentStateIndicator.FullScreenLoader -> FullScreenLoader(
            uiState = currentStateIndicator
        )

        is CurrentStateIndicator.Toasts -> Toast.makeText(
            LocalContext.current,
            currentStateIndicator.message,
            currentStateIndicator.duration
        ).show()

        else -> {}
    }
}

/**
 * Composable function for displaying a full-screen loader.
 *
 * @param message The message to display.
 * @param uiState The configuration for the full-screen loader.
 * @param onCancel The callback function to invoke when the loader is canceled.
 * @param onProceed The callback function to invoke when the user proceeds.
 */
@Composable
fun FullScreenLoader(
    uiState: CurrentStateIndicator.FullScreenLoader,
    onCancel: () -> Unit = {},
    onProceed: () -> Unit = {}
) {
    val animatedRotationAngle by rememberInfiniteTransition(label = "transition").animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000), repeatMode = RepeatMode.Restart
        ), label = "animatedRotationAngle"
    )
    Dialog(properties = DialogProperties(
        dismissOnBackPress = uiState.dismissOnBackPress,
        dismissOnClickOutside = uiState.dismissOnClickOutside,
    ), onDismissRequest = { }) {
        Card(
            modifier = Modifier.padding(15.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = uiState.backgroundColor.color)
            ),
            elevation = CardDefaults.cardElevation()
        ) {
            Column(
                modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(
                    space = 15.dp, alignment = Alignment.CenterVertically
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(54.dp)
                        .rotate(animatedRotationAngle),
                    painter = painterResource(id = R.drawable.ic_loading),
                    contentDescription = "LoaderIcon",
                    colorFilter = ColorFilter.tint(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = uiState.message, style = MaterialTheme.typography.titleMedium
                )
                if (uiState.actionAllowed) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.clickable { onCancel() },
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Button(onClick = onProceed) {
                            Text(
                                text = uiState.actionLabel
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable function for displaying a SnackBar.
 *
 * @param uiState The configuration for the SnackBar.
 * @param onAction The callback function to invoke when the action is clicked.
 */
@Composable
fun SnackBar(
    uiState: CurrentStateIndicator.SnackBar,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onAction: () -> Unit = {}
) {
    LaunchedEffect(key1 = uiState) {
        coroutineScope.launch {
            snackBarHostState.showSnackbar(
                message = uiState.message,
                duration = uiState.duration,
                actionLabel = uiState.actionLabel
            )
        }
    }
    SnackbarHost(snackBarHostState) { data ->
        Snackbar(modifier = Modifier.padding(12.dp),
            containerColor = colorResource(id = uiState.backgroundColor.color),
            contentColor = Color.Black,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = uiState.icon.icon),
                        contentDescription = "SnackBarIcon",

                        )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(data.visuals.message)
                }
            },
            action = {
                if (uiState.actionAllowed) {
                    Text(text = "Dismiss",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onAction() })
                }
            })
    }
}