package com.code.damahe.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.code.damahe.res.app.Config.MainNavigation
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.model.UserContacted
import com.code.damahe.system.model.UserProfile
import com.code.damahe.system.util.MessageUtil
import com.code.damahe.system.viewModel.MessageViewModel

@Composable
fun HomeScreen(messageViewModel: MessageViewModel = hiltViewModel(), navigate: (route: String, userProfile: UserProfile) -> Unit) {

    val userContacted = messageViewModel.userContacted.collectAsState()
    val context = LocalContext.current
    Column {
        if (userContacted.value.isEmpty()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Butt,
                )
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .systemBarsPadding()
            ) {
                items(userContacted.value) {
                    UserContactedItem(it) { profile ->
                        navigate(MainNavigation.CHAT_SCREEN_ROUTE, profile)
                    }
                }
            }
        }
    }
}

@Composable
fun UserContactedItem(userContacted: UserContacted, onClick: (userProfile: UserProfile)-> Unit) {

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
            .height(60.dp)
            .clickable { onClick(userContacted.userProfile) }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${userContacted.userProfile.firstName} ${userContacted.userProfile.lastName}",
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = MessageUtil.convertTimeMillis(userContacted.lastMessage.createdAt),
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (userContacted.myUserName == userContacted.lastMessage.chatMembers?.fromUser) "You:" else ""} ${userContacted.lastMessage.typeMessage?.text}",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )

                Spacer(modifier = Modifier.width(10.dp))

//                if (userContacted.lastMessage.seenTime == null)
//                    ReadIndicator()
            }
        }
    }
}

@Composable
fun ReadIndicator() {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}