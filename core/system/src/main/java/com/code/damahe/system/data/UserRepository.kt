package com.code.damahe.system.data

import android.content.Context
import com.code.damahe.system.database.dao.UserProfileDao
import com.code.damahe.system.firebase.AccountFirebase
import com.code.damahe.system.firebase.FirebaseDB
import com.code.damahe.system.model.UserProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(@ApplicationContext val context: Context, private val userProfileDao: UserProfileDao, val accountFirebase: AccountFirebase, val firebaseDB: FirebaseDB) {

    fun getCurrentUser() = accountFirebase.getCurrentUser()

    suspend fun getUserProfile(uid: String?): UserProfile? {
        return if (uid != null) userProfileDao.getUserProfile(uid) else null
    }

    fun getAllUser(): Flow<List<UserProfile>> {
        return userProfileDao.getAll()
    }

    suspend fun getAllList(): List<UserProfile> {
        return userProfileDao.getAllList()
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfile.uid?.let {
            if (getUserProfile(it) == null) {
                userProfileDao.insert(userProfile)
            }
        }
    }

    suspend fun removeUserProfile(userProfile: UserProfile) {
        userProfile.uid?.let {
            if (getUserProfile(it) != null) {
                userProfileDao.remove(userProfile)
            }
        }
    }
}