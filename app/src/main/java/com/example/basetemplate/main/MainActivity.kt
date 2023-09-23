package com.example.basetemplate.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.basetemplate.repo.uistatus.LoadingType
import com.example.basetemplate.ui.navigation.NavGraph
import com.example.basetemplate.ui.presentation.SnackBar
import com.example.basetemplate.ui.presentation.UiLoadingStateInfo
import com.example.basetemplate.ui.theme.BaseTemplateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    /**
     * Set up the main content of the app using Jetpack Compose.
     *
     * This function defines the layout and composition of the main user interface, including the navigation
     * graph and UI status information display.
     *
     * @param mainViewModel The ViewModel responsible for managing the app's UI status information.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            // Collect the UI status information from the mainViewModel as a State
            val loadingType by mainViewModel.loadingType.collectAsState()
            val snackBarHostState = remember { SnackbarHostState() }

            // Create the app's theme using BaseTemplateTheme
            BaseTemplateTheme {
                // Define the main Surface with a background color
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        when (val snackBar = loadingType.loadingType) {
                            is LoadingType.SnackBar -> SnackBar(
                                message = snackBar.message,
                                uiState = snackBar,
                                snackBarHostState = snackBarHostState
                            )

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
                        UiLoadingStateInfo(loadingType = loadingType.loadingType)
                    }
                }
            }
        }

    }
}