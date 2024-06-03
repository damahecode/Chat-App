package com.code.damahe.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.code.damahe.app.Activity
import com.code.damahe.app.MainContent
import com.code.damahe.ui.screen.LoginScreen

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MainContent(themeUiState = themeUiState) {
                LoginScreen(windowSizeClass = calculateWindowSizeClass(activity = this)) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}