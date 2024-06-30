package com.code.damahe.system.di

import android.content.Context
import androidx.room.Room
import com.code.damahe.system.database.AppDatabase
import com.code.damahe.system.database.dao.MessageDao
import com.code.damahe.system.database.dao.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "main1")
            .build()
    }

    @Provides
    fun provideUserProfileDao(appDatabase: AppDatabase): UserProfileDao {
        return appDatabase.userProfileDao()
    }

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
        return appDatabase.messageDao()
    }
}
