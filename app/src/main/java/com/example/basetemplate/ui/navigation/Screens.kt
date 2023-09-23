package com.example.basetemplate.ui.navigation


/**
 * Sealed class representing various screens in the app's navigation.
 *
 * @param name The name of the screen.
 * @param selectedIcon The resource ID of the selected icon for the screen (default is 0).
 * @param notSelectedIcon The resource ID of the unselected icon for the screen (default is 0).
 * @param route The route associated with the screen (default is the screen's name).
 */
sealed class Screens(
    val name: String,
    val selectedIcon: Int = 0,
    val notSelectedIcon: Int = 0,
    val route: String = name
) {
    /**
     * Represents the "Post" screen.
     */
    data object Post : Screens(name = POST, route = "$POST/{$postArgs}") {
        fun postIdArgs(postArgs: Int): String {
            return "$POST/$postArgs"
        }
    }

    /**
     * Represents the "PostList" screen.
     */
    data object PostList : Screens(name = POST_LIST)

    companion object {
        /**
         * Routes for the screens.
         */
        private const val POST = "Post"
        private const val POST_LIST = "PostList"


        /**
         * Arguments Ids*/
        const val postArgs = "PostPageArgs"
    }
}
