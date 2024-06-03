package com.code.damahe.system.firebase

import android.util.Log
import com.code.damahe.system.model.MembersUID
import com.code.damahe.system.model.Message
import com.code.damahe.system.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirebaseDB(private val db: FirebaseFirestore) {

    suspend fun insertUser(profile: UserProfile, callback: (success: Boolean) -> Unit) {
        profile.uid?.let {
            db.collection("users")
                .document(it)
                .set(profile, SetOptions.merge())
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener { e ->
                    callback(false)
                    Log.w(this::class.java.name, "Error writing document", e)
                }
        }
    }

    suspend fun getUserDetails(uid: String, callback: (profile: UserProfile?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    callback(document.toObject(UserProfile::class.java))
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                callback(null)
                Log.d(this::class.java.name, "get failed with ", exception)
            }
    }

    suspend fun getUserDetails(field: String, values: List<String>, callback: (userList: List<UserProfile>) -> Unit) {
        if (values.isNotEmpty()) {
            val list = ArrayList<UserProfile>()
            db.collection("users")
                .whereIn(field, values).get()
                .addOnSuccessListener {
                    for (queryDocument in it) {
                        list.add(queryDocument.toObject(UserProfile::class.java))
                    }
                    callback(list)
                }
                .addOnFailureListener {
                    callback(emptyList())
                    Log.d(this::class.java.name, "get failed with ", it)
                }
        }
    }

    suspend fun getUserContacted(uid: String, callback: (msUID: List<MembersUID>) -> Unit) {
        val list = ArrayList<MembersUID>()
        db.collection("MessageRoom")
            .whereArrayContainsAny("membersUid", listOf(uid))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("getUserContacted", "get failed with ", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    Log.d("getUserContacted", value.size().toString())
                    for (queryDocument in value) {
                        list.add(queryDocument.toObject(MembersUID::class.java))
                    }
                }
                callback(list)
            }
    }

    suspend fun getJointUID(fromUid: String, toUid: String, create: Boolean = false, callback: (jointUID: String?) -> Unit) {
        db.collection("MessageRoom").document("${fromUid}_${toUid}").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    callback("${fromUid}_${toUid}")
                } else {
                    db.collection("MessageRoom").document("${toUid}_${fromUid}").get()
                        .addOnSuccessListener { documentSnapshot2 ->
                            if (documentSnapshot2.exists()) {
                                callback("${toUid}_${fromUid}")
                            } else if (create) {
                                db.collection("MessageRoom")
                                    .document("${fromUid}_${toUid}")
                                    .set(MembersUID(listOf(fromUid, toUid)), SetOptions.merge())
                                    .addOnSuccessListener {
                                        callback("${fromUid}_${toUid}")
                                    }
                                    .addOnFailureListener { e ->
                                        callback(null)
                                        Log.w(this::class.java.name, "Error writing document", e)
                                    }
                            } else
                                callback(null)
                        }
                }
            }
    }

    suspend fun getUserMessages(jointUID: String, callback: (list: List<Message>) -> Unit) {
        val list = ArrayList<Message>()
        db.collection("MessageRoom").document(jointUID)
            .collection("messages").get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    for (message in value) {
                        list.add(message.toObject(Message::class.java))
                    }
                    callback(list)
                }
            }
    }

    suspend fun sendMessage(jointUID: String, message: Message, callback: (success: Boolean) -> Unit) {

        message.status = 1  // changing message status to sent

        db.collection("MessageRoom")
            .document(jointUID)
            .collection("messages")
            .document(message.documentId)
            .set(message, SetOptions.merge())
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                callback(false)
                Log.w(this::class.java.name, "Error writing document", e)
            }
    }
}