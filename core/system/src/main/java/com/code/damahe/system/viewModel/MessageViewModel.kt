package com.code.damahe.system.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.damahe.system.data.MessageRepository
import com.code.damahe.system.data.UserRepository
import com.code.damahe.system.model.ChatMembers
import com.code.damahe.system.model.Message
import com.code.damahe.system.model.TypeMessage
import com.code.damahe.system.model.UserContacted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val messageRepository: MessageRepository, private val userRepository: UserRepository): ViewModel() {

    init {
        fetchUserContacted()
    }

    val userContacted = MutableStateFlow<List<UserContacted>>(emptyList())

    val myMessages = MutableStateFlow<List<Message>>(emptyList())

    private fun fetchUserContacted(myUid: String? = userRepository.getCurrentUser()) {
        viewModelScope.launch(Dispatchers.IO) {
            if (myUid != null) {
                messageRepository.getUserContacted(myUid) {
                    val uidList = ArrayList<String>()
                    for (msUID in it) {
                        for (uid in msUID.membersUid) {
                            if (uid != myUid) {
                                uidList.add(uid)
                            }
                        }
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        val list = ArrayList<UserContacted>()
                        messageRepository.fetchUserContacted(myUid, uidList) { contacted ->
                            list.add(contacted)
                            updateUserContacted(list)
                        }
                    }
                }
            }
        }
    }

    private fun updateUserContacted(list: List<UserContacted>) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            userContacted.emit(list)
        }
    }

    fun fetchMessages(fromUid: String, toUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.getJointUID(fromUid, toUid) {
                if (it != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        messageRepository.firebaseDB.getUserMessages(it) { list ->
                            updateMyMessages(list)
                        }
                    }
                }
            }
        }
    }

    private fun updateMyMessages(list: List<Message>) {
        viewModelScope.launch(Dispatchers.IO) {
            myMessages.emit(list)
        }
    }

    fun sendMessage(fromUid: String, toUid: String, chatMembers: ChatMembers, type: String, typeMessage: TypeMessage, callback: (success: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val message = Message(
                chatMembers = chatMembers,
                type = type,
                typeMessage = typeMessage
            )
            messageRepository.getJointUID(fromUid, toUid, true) {
                if (it != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        messageRepository.firebaseDB.sendMessage(it, message, callback)
                    }
                } else
                    callback(false)
            }
        }
    }
}