package com.code.damahe.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.code.damahe.res.icon.DCodeIcon
import com.code.damahe.res.icon.DrawIcon
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.viewModel.UserViewModel

@Composable
fun LoginScreen(windowSizeClass: WindowSizeClass, viewModel: UserViewModel = hiltViewModel(), onSuccessfulLogin: () -> Unit) {

    val context = LocalContext.current
    val isOnline = viewModel.isOnline.collectAsState(initial = false)

    Scaffold { paddingValues ->

        var signInMode by remember { mutableStateOf( true ) }
        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            //item { LottieWorkingLoadingView(context = LocalContext.current) }
            item {
                Text(
                    text = if (signInMode) "Welcome Back" else "Create an Account",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = if (signInMode) "We have missed you, Let's start by Sign In!" else "Create an Account by filling in info below.",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = email,
                    leadingIcon = {
//                        FaIcon(
//                            faIcon = FaIcons.Envelope,
//                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
//                        )
                    },
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "Email address") },
                    placeholder = { Text(text = "abc@gmail.com") },
                    onValueChange = {
                        email = it
                    },
                    interactionSource = emailInteractionState,
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    leadingIcon = {
//                        FaIcon(
//                            faIcon = FaIcons.Key,
//                            tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
//                        )
                    },
                    trailingIcon = {
                        DrawIcon(icon = DCodeIcon.ImageVectorIcon(MyIcons.AccountCircle),
                            contentDescription = null,
                            modifier = Modifier.clickable(onClick = {
                                passwordVisualTransformation =
                                    if (passwordVisualTransformation != VisualTransformation.None) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    }
                            })
                        )
                    },
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "12334444") },
                    onValueChange = {
                        password = it
                    },
                    interactionSource = passwordInteractionState,
                    visualTransformation = passwordVisualTransformation,
                )
            }
            item {
                var loading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        if (invalidInput(email.text, password.text)) {
                            hasError = true
                            loading = false
                        } else {
                            loading = true
                            hasError = false

                            if (isOnline.value) {
                                if (signInMode) {
                                    viewModel.loginUserAccount(email.text, password.text) { uid ->
                                        if (uid != null)
                                            onSuccessfulLogin()
                                        else
                                            loading = false
                                    }
                                } else {
                                    viewModel.createUserAccount(email.text, password.text) { uid ->
                                        if (uid != null)
                                            onSuccessfulLogin()
                                        else
                                            loading = false
                                    }
                                }
                            } else {
                                loading = false
                                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (loading) {
                        //HorizontalDottedProgressBar()
                        Text(text = "Please wait...")
                    } else {
                        if (signInMode) Text(text = "Log In") else Text(text = "Sign Up")
                    }
                }
            }
//            item {
//                Box(modifier = Modifier.padding(vertical = 16.dp)) {
//                    Spacer(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .height(1.dp)
//                            .fillMaxWidth()
//                            .background(Color.LightGray)
//                    )
//                    Text(
//                        text = "Or use",
//                        color = Color.LightGray,
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .background(MaterialTheme.colorScheme.background)
//                            .padding(horizontal = 16.dp)
//                    )
//                }
//            }
//
//            item {
//                OutlinedButton(
//                    onClick = { }, modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                ) {
////                    FaIcon(
////                        faIcon = FaIcons.Facebook,
////                        tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
////                    )
//                    Text(
//                        text = "Sign in with Facebook",
//                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
//
//            item { Spacer(modifier = Modifier.height(8.dp)) }
//
//            item {
//                OutlinedButton(
//                    onClick = { }, modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp)
//                ) {
////                    FaIcon(
////                        faIcon = FaIcons.Google,
////                        tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
////                    )
//                    Text(
//                        text = "Sign in with Gmail",
//                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }

            item {
                val primaryColor = MaterialTheme.colorScheme.primary
                val annotatedString = remember {
                    AnnotatedString.Builder(if (signInMode) "Don't have an account? Register" else "Already have an account? Sign in")
                        .apply {
                            if (signInMode)
                                addStyle(style = SpanStyle(color = primaryColor), 23, 31)
                            else
                                addStyle(style = SpanStyle(color = primaryColor), 25, 32)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(onClick = {
                            signInMode = !signInMode
                        }),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

fun invalidInput(email: String, password: String) =
    email.isBlank() || password.isBlank()