package com.code.damahe.system.database

import androidx.room.TypeConverter
import com.code.damahe.system.model.ChatMembers
import com.code.damahe.system.model.TypeMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatMembersTypeConverter {
    @TypeConverter
    fun fromType(chatMembers: ChatMembers): String {
        val gson = Gson()
        return gson.toJson(chatMembers)
    }

    @TypeConverter
    fun toType(chatMembers: String): ChatMembers {
        val type = object : TypeToken<ChatMembers>() {}.type
        return Gson().fromJson(chatMembers, type)
    }
}

class TypeMessageTypeConverter {
    @TypeConverter
    fun fromType(typeMessage: TypeMessage): String {
        val gson = Gson()
        return gson.toJson(typeMessage)
    }

    @TypeConverter
    fun toType(typeMessage: String): TypeMessage {
        val type = object : TypeToken<ChatMembers>() {}.type
        return Gson().fromJson(typeMessage, type)
    }
}