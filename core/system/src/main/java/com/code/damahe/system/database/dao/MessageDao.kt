package com.code.damahe.system.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.code.damahe.system.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE documentId = :documentId")
    fun getAll(documentId: String): Flow<List<Message>>

    @Insert
    suspend fun insert(message: Message)

    @Delete
    suspend fun remove(message: Message)
}