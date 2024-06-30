package com.code.damahe.system.data

import com.code.damahe.system.database.dao.MessageDao
import com.code.damahe.system.firebase.FirebaseDB
import com.code.damahe.system.model.MembersUID
import com.code.damahe.system.model.UserContacted
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(val firebaseDB: FirebaseDB, val messageDao: MessageDao) {

    suspend fun getJointUID(fromUid: String, toUid: String, createUID: Boolean = false, callback: (jointUID: String?) -> Unit) {
        firebaseDB.getJointUID(fromUid, toUid, createUID, callback)
    }

    suspend fun getUserContacted(uid: String, callback: (list: List<MembersUID>) -> Unit) {
        firebaseDB.getUserContacted(uid, callback)
    }

    suspend fun fetchUserContacted(myUID: String, uidList: List<String>, callback: (contacted: UserContacted) -> Unit)  {
        uidList.forEach { uid ->
            getJointUID(myUID, uid) { jointUID ->
                firebaseDB.getUser(uid) { profile ->
                    if (jointUID != null && profile != null) {
                        firebaseDB.getUserLastMessage(jointUID) { message ->
                            callback(UserContacted(myUID, profile, message))
                        }
                    }
                }
            }
        }
    }
}