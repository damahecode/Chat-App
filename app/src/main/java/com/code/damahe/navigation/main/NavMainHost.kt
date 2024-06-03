package com.code.damahe.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.code.damahe.navigation.NavAppState
import com.code.damahe.res.app.Config.MainNavigation
import com.code.damahe.screen.ChatScreen
import com.code.damahe.screen.MainScreen

@Composable
fun NavMainHost(
    navAppState: NavAppState,
) {

    val navController = navAppState.navController

    NavHost(
        navController = navController,
        startDestination = MainNavigation.HOME_SCREEN_ROUTE,
    ) {
        composable(route = MainNavigation.HOME_SCREEN_ROUTE) {
            MainScreen(navAppState.activity) { route, user ->
                navAppState.userProfile = user
                navAppState.navigateToDestination(route)
            }
        }

        composable(route = MainNavigation.CHAT_SCREEN_ROUTE) {
            ChatScreen(navAppState.userProfile)
        }
    }
}