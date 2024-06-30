package com.code.damahe.system.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"
)
data class Message(
    @PrimaryKey
    var documentId: String = System.currentTimeMillis().toString(), // unique id for db collection("messages")
    var chatMembers: ChatMembers? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var type: String? = null, // 0=text, 1=audio, 2=image, 3=video, 4=file
    var status: Int = 0, // 0=sending, 1=sent, 2=delivered, 3=seen, 4=failed
    var typeMessage: TypeMessage? = null,
    var deliveryTime: Long? = null,
    var seenTime: Long? = null
)

data class ChatMembers(
    var fromUser: String? = null, // userUID
    var toUser: String? = null, // userUID (personal/group)
)

data class TypeMessage(
    var text: String? = null
)

data class MembersUID(
    var membersUid: List<String> = emptyList()
)