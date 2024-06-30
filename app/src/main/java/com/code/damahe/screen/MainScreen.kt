/*
 * Copyright (c) 2024 damahecode.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.code.damahe.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.code.damahe.activity.LoginActivity
import com.code.damahe.res.R
import com.code.damahe.res.icon.DCodeIcon.ImageVectorIcon
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.material.theme.DCodeBackground
import com.code.damahe.material.theme.DCodeGradientBackground
import com.code.damahe.material.dialogs.ThemeDialog
import com.code.damahe.material.model.ThemeString
import com.code.damahe.res.icon.DrawIcon
import com.code.damahe.system.model.UserProfile
import com.code.damahe.system.viewModel.MessageViewModel
import com.code.damahe.system.viewModel.UserViewModel
import com.code.damahe.ui.dialog.ProfileScreen
import com.code.damahe.ui.dialog.UserListScreen
import com.code.damahe.ui.screen.HomeScreen

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(activity: Activity, viewModel: UserViewModel = hiltViewModel(), navigate: (route: String, userProfile: UserProfile) -> Unit) {

    val showThemeSettingsDialog = remember { mutableStateOf(false) }
    val currentUser = viewModel.currentUser.collectAsState()

    val openProfileSheet = remember { mutableStateOf(false) }
    val openUserListSheet = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showThemeSettingsDialog.value) {
        ThemeDialog(
            string = ThemeString(R.string.title_app_theme, R.string.loading, R.string.ok, R.string.brand_default,
                R.string.brand_dynamic, R.string.gradient_colors_preference, R.string.gradient_colors_yes,
                R.string.gradient_colors_no, R.string.dark_mode_preference, R.string.dark_mode_config_system_default,
                R.string.dark_mode_config_light, R.string.dark_mode_config_dark),
            onDismiss = {showThemeSettingsDialog.value = false},
        )
    }

    // Sheet content
    if (openProfileSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                openProfileSheet.value = false
            },
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0)
        ) {
            ProfileScreen(viewModel)
        }
    }

    if (openUserListSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                openUserListSheet.value = false
            },
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0)
        ) {
            UserListScreen { route, profile ->
                navigate(route, profile)
            }
        }
    }

    viewModel.checkLoginStatus {
        if (it == null) {
            openProfileSheet.value = true
        }
    }
    if (currentUser.value == null) {
        viewModel.removeAllUserProfile()
        activity.startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }

    DCodeBackground {
        DCodeGradientBackground {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0),
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name)) },
                        navigationIcon = {
                            Row {
                                IconButton(onClick = { showThemeSettingsDialog.value = true }) {
                                    DrawIcon(icon = ImageVectorIcon(MyIcons.Settings), contentDescription = stringResource(id = R.string.txt_preferences))
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { openProfileSheet.value = true }) {
                                DrawIcon(icon = ImageVectorIcon(MyIcons.AccountCircle), contentDescription = "Profile")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        openUserListSheet.value = true
                    },
                        modifier = Modifier.systemBarsPadding()) {
                        DrawIcon(icon = ImageVectorIcon(MyIcons.Add), contentDescription = "Start Chat")
                    }
                }
            ) { padding ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        ),
                ) {
                    HomeScreen(navigate = navigate)
                }
            }
        }
    }
}