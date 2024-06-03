package com.code.damahe.system.di

import android.content.Context
import com.code.damahe.system.data.ConnectivityRepository
import com.code.damahe.system.firebase.AccountFirebase
import com.code.damahe.system.firebase.FirebaseDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun providesAccountFirebase(): AccountFirebase {
        return AccountFirebase(FirebaseAuth.getInstance())
    }

    @Provides
    @Singleton
    fun providesFirebaseDB(): FirebaseDB {
        return FirebaseDB(FirebaseFirestore.getInstance())
    }

    @Provides
    @Singleton
    fun providesConnectivity(@ApplicationContext context: Context): ConnectivityRepository {
        return ConnectivityRepository(context)
    }
}