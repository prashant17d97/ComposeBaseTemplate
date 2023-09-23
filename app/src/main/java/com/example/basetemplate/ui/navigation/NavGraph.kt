package com.example.basetemplate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.basetemplate.ui.navigation.Screens.Companion.postArgs
import com.example.basetemplate.ui.presentation.postlist.Post
import com.example.basetemplate.ui.presentation.postlist.PostList

/**
 * Composable function defining the navigation graph for the app.
 *
 * @param navHostController The navigation controller responsible for handling navigation within the app.
 */
@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.PostList.route) {
        composable(Screens.PostList.route) {
            PostList(navHostController = navHostController)
        }
        composable(Screens.Post.route, arguments = listOf(
            navArgument(postArgs) {
                type = NavType.IntType
            }
        )) {
            val id = it.arguments?.getInt(postArgs,0)
            Post(id = id)
        }
    }
}