package com.code.damahe.system.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AccountFirebase(private val auth: FirebaseAuth) {

    /**
     * @return uid of current user
     */
    fun getCurrentUser(): String? {
        var uid: String? = null
        if (auth.currentUser != null)
            uid = auth.currentUser?.uid

        return uid
    }

    /**
     * @return email of current user
     */
    fun getUserEmail(): String? {
        var email: String? = null
        if (auth.currentUser != null)
            email = auth.currentUser?.email

        return email
    }

    /**
     * Create new user with email and password using Firebase Authentication
     * @param email enter your email id
     * @param password create your password
     * @param callback: (uid: String?, email: String?) -> Unit
     */
    fun register(email: String, password: String, callback: (uid: String?) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.result.user?.uid)
            }
            .addOnFailureListener { exception ->
                callback(null)
                Log.d(this::class.java.name, exception.message ?: "register error")
            }
    }

    /**
     * Login with email and password using Firebase Authentication
     * @param email enter your email id
     * @param password enter your password
     * @param callback: (uid: String?) -> Unit
     */
    fun login(email: String, password: String, callback: (uid: String?) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.result.user?.uid)
            }
            .addOnFailureListener { exception ->
                callback(null)
                Log.d(this::class.java.name, exception.message ?: "register error")
            }
    }

    fun logout(){
        auth.signOut()
    }

}