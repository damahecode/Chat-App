package com.code.damahe.system.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.code.damahe.system.database.dao.UserProfileDao
import com.code.damahe.system.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        //Message::class
    ],
    views = [],
    version = 1,
    exportSchema = false
)
//@TypeConverters(
//    ChatMembersTypeConverter::class, TypeMessageTypeConverter::class
//)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    //abstract fun messageDao(): MessageDao
}