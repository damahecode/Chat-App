package com.code.damahe.system.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.code.damahe.system.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {

    @Query("SELECT * FROM userProfile WHERE uid = :uid")
    suspend fun getUserProfile(uid: String): UserProfile?

    @Query("SELECT * FROM userProfile ORDER BY firstName DESC")
    suspend fun getAllList(): List<UserProfile>

    @Query("SELECT * FROM userProfile ORDER BY firstName DESC")
    fun getAll(): Flow<List<UserProfile>>

    @Insert
    suspend fun insert(userProfile: UserProfile)

    @Delete
    suspend fun remove(userProfile: UserProfile)
}
