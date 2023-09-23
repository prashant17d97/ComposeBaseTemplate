package com.example.basetemplate.main

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.basetemplate.repo.uistatus.COLOR
import com.example.basetemplate.repo.uistatus.CurrentStateIndicator
import com.example.basetemplate.repo.uistatus.Icon
import com.example.basetemplate.ui.navigation.NavGraph
import com.example.basetemplate.ui.presentation.SnackBar
import com.example.basetemplate.ui.presentation.UiLoadingStateInfo
import com.example.basetemplate.ui.theme.BaseTemplateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()
    private var isOnline = false // Flag to track network status for the first launch check

    /**
     * Set up the main content of the app using Jetpack Compose.
     *
     * This function defines the layout and composition of the main user interface, including the navigation
     * graph and UI status information display.
     *
     */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // Register network callback for updates
        registerNetworkCallback()
        setContent {
            // Collect the UI status information from the mainViewModel as a State
            val currentStateIndicator by mainViewModel.currentLoadingStateIndicator.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }

            // Create the app's theme using BaseTemplateTheme
            BaseTemplateTheme {
                // Define the main Surface with a background color
                val coroutine = rememberCoroutineScope()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        when (val snackBar = currentStateIndicator) {
                            is CurrentStateIndicator.SnackBar -> {
                                SnackBar(
                                    uiState = snackBar,
                                    snackBarHostState = snackBarHostState,
                                    coroutineScope = coroutine
                                )

                            }

                            else -> {}
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        // Set up the navigation graph using NavGraph
                        NavGraph(navHostController = rememberNavController())

                        // Display UI status information using UiStatesInfo
                        UiLoadingStateInfo(currentStateIndicator = currentStateIndicator)
                    }
                }
            }
        }
    }

    private fun registerNetworkCallback() {
        val connectivityManager: ConnectivityManager =
            getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (isOnline) {
                    mainViewModel.updateLoadingState(
                        CurrentStateIndicator.SnackBar(
                            message = "We're back Online! 🥳",
                            actionAllowed = true,
                            actionLabel = "Reload",
                            icon = Icon.INFO,
                            backgroundColor = COLOR.SUCCESS
                        )
                    )
                }
                mainViewModel.setNetWorkAvailability(isAvailable = true)

            }

            override fun onLost(network: Network) {
                mainViewModel.updateLoadingState(
                    CurrentStateIndicator.SnackBar(
                        message = "You're Offline!",
                        actionAllowed = true,
                        actionLabel = "Turn on",
                        icon = Icon.WARNING,
                        backgroundColor = COLOR.WARNING
                    )
                )
                mainViewModel.setNetWorkAvailability(isAvailable = false)
                isOnline = true
            }
        })
    }

}