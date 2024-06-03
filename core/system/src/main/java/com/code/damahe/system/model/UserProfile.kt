package com.code.damahe.system.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "userProfile"
)
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var uid: String? = null,
    var userName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var profileImageUrl: String? = null,
    var about: String? = null,
    var fcmToken: String? = null,
)
