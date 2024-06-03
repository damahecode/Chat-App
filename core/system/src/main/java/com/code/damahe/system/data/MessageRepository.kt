package com.code.damahe.system.data

import com.code.damahe.system.firebase.FirebaseDB
import com.code.damahe.system.model.MembersUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepository @Inject constructor(val firebaseDB: FirebaseDB) {

    suspend fun getJointUID(fromUid: String, toUid: String, createUID: Boolean = false, callback: (jointUID: String?) -> Unit) {
        firebaseDB.getJointUID(fromUid, toUid, createUID, callback)
    }

    suspend fun getUserContacted(uid: String, callback: (list: List<MembersUID>) -> Unit) {
        firebaseDB.getUserContacted(uid, callback)
    }
}