package com.code.damahe.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.code.damahe.material.theme.DCodeBackground
import com.code.damahe.material.theme.DCodeGradientBackground
import com.code.damahe.material.theme.GradientColors
import com.code.damahe.res.icon.DCodeIcon
import com.code.damahe.res.icon.DrawIcon
import com.code.damahe.res.R
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.model.ChatMembers
import com.code.damahe.system.model.Message
import com.code.damahe.system.model.TypeMessage
import com.code.damahe.system.model.UserProfile
import com.code.damahe.system.viewModel.MessageViewModel
import com.code.damahe.system.viewModel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(toUser: UserProfile?, userViewModel: UserViewModel = hiltViewModel(), messageViewModel: MessageViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val myProfile = userViewModel.myProfile.collectAsState()
    val myMessages = messageViewModel.myMessages.collectAsState()

    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            while (true) {
                if (myProfile.value?.uid != null && toUser?.uid != null) {
                    messageViewModel.fetchMessages(myProfile.value?.uid!!, toUser.uid!!)
                }
                delay(3.seconds)
            }
        }
    }

    DCodeBackground {
        DCodeGradientBackground(gradientColors = GradientColors()) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0),
                topBar = {
                    TopAppBar(
                        title = { Text(text = "${toUser?.firstName ?: ""} ${toUser?.lastName ?: ""}") },
                        navigationIcon = {
                            Row {
                                IconButton(onClick = { }) {
                                    DrawIcon(
                                        icon = DCodeIcon.ImageVectorIcon(MyIcons.AccountCircle),
                                        contentDescription = stringResource(id = R.string.app_name)
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                DrawIcon(
                                    icon = DCodeIcon.ImageVectorIcon(MyIcons.More),
                                    contentDescription = stringResource(id = R.string.txt_preferences)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }
            ) { padding ->
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        )
                ) {
                    val (messages, chatBox) = createRefs()

                    val listState = rememberLazyListState()
                    LaunchedEffect(myMessages.value.size) {
                        listState.animateScrollToItem(myMessages.value.size)
                    }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(messages) {
                                top.linkTo(parent.top)
                                bottom.linkTo(chatBox.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                height = Dimension.fillToConstraints
                            },
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(myMessages.value) { item ->
                            ChatItem(toUser?.userName != item.chatMembers?.fromUser, item)
                        }
                    }
                    ChatBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .systemBarsPadding()
                            .constrainAs(chatBox) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        if (myProfile.value?.uid != null && toUser?.uid != null) {
                            messageViewModel.sendMessage(myProfile.value?.uid!!, toUser.uid!!,
                                ChatMembers(myProfile.value?.userName, toUser.userName),
                                "text", TypeMessage(it)) { success ->
                                if (!success)
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            if (myProfile.value?.uid == null)
                                Toast.makeText(context, "myProfile.value?.uid != null", Toast.LENGTH_SHORT).show()
                            if (toUser?.uid == null)
                                Toast.makeText(context, "toUser?.uid != null", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(isFromMe: Boolean, message: Message) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = 4.dp, bottom = 4.dp,
            start = if (isFromMe) 40.dp else 4.dp,
            end = if (isFromMe) 4.dp else 40.dp
        )
    ) {
        Box(
            modifier = Modifier
                .align(if (isFromMe) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 48f,
                        topEnd = 48f,
                        bottomStart = if (isFromMe) 48f else 0f,
                        bottomEnd = if (isFromMe) 0f else 48f
                    )
                )
                .background(if (isFromMe) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(text = message.typeMessage?.text ?: "")
        }
    }
}

@Composable
fun ChatBox(
    modifier: Modifier,
    onSendChatClickListener: (String) -> Unit,
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        TextField(
            maxLines = 5,
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(text = "Type something")
            }
        )
        IconButton(
            onClick = {
                val msg = chatBoxValue.text
                if (msg.isBlank()) return@IconButton
                onSendChatClickListener(chatBoxValue.text)
                chatBoxValue = TextFieldValue("")
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}