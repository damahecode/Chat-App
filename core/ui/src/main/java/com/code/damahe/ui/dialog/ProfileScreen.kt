package com.code.damahe.ui.dialog

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.code.damahe.res.icon.DCodeIcon
import com.code.damahe.res.icon.DrawIcon
import com.code.damahe.res.icon.MyIcons
import com.code.damahe.system.viewModel.UserViewModel

@Composable
fun ProfileScreen(viewModel: UserViewModel) {

    val context = LocalContext.current
    val myProfile = viewModel.myProfile.collectAsState()
    var createServerProfileMode by remember { mutableStateOf(false) }

    createServerProfileMode = myProfile.value == null

    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var about by remember { mutableStateOf(TextFieldValue("")) }

    var hasError by remember { mutableStateOf(false) }

    Surface {
        Box(modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (!createServerProfileMode) {
                    Row(
                        modifier = Modifier.padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        DrawIcon(
                            icon = DCodeIcon.ImageVectorIcon(MyIcons.AccountCircle),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(60.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "${myProfile.value?.firstName ?: ""} ${myProfile.value?.lastName ?: ""}",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                text = myProfile.value?.userName ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = myProfile.value?.about ?: "",
                        style = MaterialTheme.typography.bodySmall,
                    )
                } else {

                    OutlinedTextField(
                        value = firstName,
                        maxLines = 1,
                        isError = firstName.text.isBlank(),
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = { Text(text = "First Name") },
                        placeholder = { Text(text = "Your Name") },
                        onValueChange = {
                            firstName = it
                        }
                    )

                    OutlinedTextField(
                        value = lastName,
                        maxLines = 1,
                        isError = lastName.text.isBlank(),
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = { Text(text = "Last Name") },
                        placeholder = { Text(text = "Your Last Name") },
                        onValueChange = {
                            lastName = it
                        }
                    )

                    OutlinedTextField(
                        value = userName,
                        maxLines = 1,
                        isError = userName.text.isBlank() || hasError,
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = { Text(text = "Create Unique UserName") },
                        placeholder = { Text(text = "user-1") },
                        onValueChange = {
                            userName = it
                            viewModel.getUserProfile(userName.text) { profile ->
                                hasError = profile != null
                            }
                        },
                        supportingText = { if (hasError) Text(text = "UserName is already exist") }
                    )

                    OutlinedTextField(
                        value = about,
                        minLines = 2,
                        maxLines = 4,
                        isError = about.text.isBlank(),
                        modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 15.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        label = { Text(text = "About") },
                        placeholder = { Text(text = "Your Info..") },
                        onValueChange = {
                            about = it
                        }
                    )
                }

                Button(
                    onClick = {
                        if (createServerProfileMode) {
                            if (userName.text.isNotBlank()) {
                                if (!hasError) {
                                    viewModel.createServerProfile(
                                        userName.text, firstName.text, lastName.text,
                                        "https://img.png", about.text
                                    ) { success ->
                                        createServerProfileMode = !success
                                        if (success)
                                            Toast.makeText(context, "create Profile success", Toast.LENGTH_LONG).show()
                                        else
                                            Toast.makeText(context, "Fail to create Profile", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        } else {
                            viewModel.logOut()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (createServerProfileMode) {
                        Text(
                            text = "Create Profile",
                            style = MaterialTheme.typography.labelMedium
                        )
                    } else {
                        Text(
                            text = "Log out",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}