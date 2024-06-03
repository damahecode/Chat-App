package com.code.damahe.navigation

import android.app.Activity
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.code.damahe.system.model.UserProfile

@Composable
fun rememberNavAppState(
    activity: Activity,
    navController: NavHostController = rememberNavController(),
    windowSizeClass: WindowSizeClass
): NavAppState {
    return remember(
        navController,
        windowSizeClass
    ) {
        NavAppState(
            activity = activity,
            navController = navController,
            windowSizeClass = windowSizeClass
        )
    }
}

@Stable
class NavAppState(
    val activity: Activity,
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigateToDestination(route: String) {
        navController.navigate(route)
    }

    var userProfile: UserProfile? = null
}