package com.code.damahe.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.code.damahe.res.app.Config.MainNavigation
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.model.UserProfile
import com.code.damahe.system.viewModel.MessageViewModel

@Composable
fun HomeScreen(messageViewModel: MessageViewModel, navigate: (route: String, userProfile: UserProfile) -> Unit) {

    val userContacted = messageViewModel.userContacted.collectAsState()

    Column {

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .systemBarsPadding()
        ) {
            items(userContacted.value) {
                UserProfileItem(it) { profile ->
                    navigate(MainNavigation.CHAT_SCREEN_ROUTE, profile)
                }
            }
        }
    }
}

@Composable
fun UserProfileItem(userProfile: UserProfile, click: (userProfile: UserProfile)-> Unit) {

//    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(context)
//            .error(R.drawable.music_note_24)
//            .data(music.artUri)
//            .build()
//    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = { click(userProfile) }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .padding(2.dp),
                imageVector = MyIcons.AccountCircle,
                contentDescription = "Song cover"
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "${userProfile.firstName ?: ""} ${userProfile.lastName ?: ""}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}