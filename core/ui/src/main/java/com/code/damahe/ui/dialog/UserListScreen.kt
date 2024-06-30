package com.code.damahe.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.code.damahe.res.app.Config
import com.code.damahe.res.icon.DCodeIcon
import com.code.damahe.res.icon.DrawIcon
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.model.UserProfile
import com.code.damahe.system.viewModel.UserViewModel
import com.code.damahe.res.R
import com.code.damahe.ui.screen.UserContactedItem

@Composable
fun UserListScreen(userViewModel: UserViewModel = hiltViewModel(), navigate: (route: String, userProfile: UserProfile) -> Unit) {
    val allUser = userViewModel.getAllUser.collectAsState(initial = emptyList())

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AddUserDialog(userViewModel) {
            showDialog.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    ) {
        Button(
            onClick = { showDialog.value = true },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            DrawIcon(
                icon = DCodeIcon.ImageVectorIcon(MyIcons.Add),
                contentDescription = "Add users",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Add new user")
        }

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ) {
            items(allUser.value) {
                UserContactItem(it) { profile ->
                    navigate(Config.MainNavigation.CHAT_SCREEN_ROUTE, profile)
                }
            }
        }
    }
}

@Composable
fun AddUserDialog(userViewModel: UserViewModel, onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current

    var userProfile: UserProfile? = null

    var userName by remember { mutableStateOf(TextFieldValue("")) }
    val userNameInteractionState = remember { MutableInteractionSource() }
    var hasError by remember { mutableStateOf(false) }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Add User",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Divider(Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = userName,
                    maxLines = 1,
                    isError = userName.text.isBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text(text = "User Name") },
                    placeholder = { Text(text = "user-1") },
                    onValueChange = {
                        userName = it
                        userViewModel.getUserProfile(userName.text) { profile ->
                            hasError = profile == null
                            userProfile = profile
                        }
                    },
                    interactionSource = userNameInteractionState,
                    supportingText = {
                        if (hasError) Text(text = "No User Found")
                    }
                )
                Divider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = stringResource(id = R.string.ok),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        if (userName.text.isNotBlank() && userProfile != null) {
                            userViewModel.saveUserProfile(userProfile!!)
                            onDismiss()
                        }
                    },
            )
        }
    )
}

@Composable
fun UserContactItem(userProfile: UserProfile, onClick: (userProfile: UserProfile)-> Unit) {

//    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context)
//            .error(R.drawable.music_note_24)
//            .data(music.artUri)
//            .build()
//    )

    Row(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick(userProfile) }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = MyIcons.AccountCircle,
                // painter = painterResource(id = userProfile.profileImageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .height(50.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = "${userProfile.firstName} ${userProfile.lastName}",
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(
                text = userProfile.about ?: "",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        }
    }
}