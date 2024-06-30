package com.code.damahe.system.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code.damahe.system.data.ConnectivityRepository
import com.code.damahe.system.data.UserRepository
import com.code.damahe.system.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository, connectivity: ConnectivityRepository) : ViewModel() {

    init {
        checkLoginStatus{}
    }

    val isOnline = connectivity.isConnected

    /**
     * @return uid of current user
     */
    var currentUser = MutableStateFlow(userRepository.getCurrentUser())

    private fun getUserEmail() = userRepository.accountFirebase.getUserEmail()

    val myProfile: MutableStateFlow<UserProfile?> = MutableStateFlow(null)

    val getAllUser: Flow<List<UserProfile>>
        get() = userRepository.getAllUser()

    fun checkLoginStatus(action: (profile: UserProfile?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.emit(userRepository.getCurrentUser())

            checkMyServerProfile {
                if (it == null)
                    removeAllUserProfile()
                else  {
                    viewModelScope.launch(Dispatchers.IO) {
                        val profile = userRepository.getUserProfile(userRepository.getCurrentUser())
                        if (profile == null)
                            saveUserProfile(it)
                    }
                }
            }
            delay(3.seconds)
            val profile2 = userRepository.getUserProfile(userRepository.getCurrentUser())
            myProfile.emit(profile2)
            action(profile2)
        }
    }

    private fun checkMyServerProfile(callback: (serverProfile: UserProfile?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getCurrentUser()?.let {
                userRepository.firebaseDB.getUser(it) { profile ->
                    callback(profile)
                }
            }
        }
    }

    fun getUserProfile(userName: String, callback: (userProfile: UserProfile?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.firebaseDB.getUsers("userName", listOf(userName)) {
                if (it.size == 1) {
                    callback(it[0])
                } else {
                    callback(null)
                }
            }
        }
    }

    fun createUserAccount(email: String, password: String, callback: (uid: String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.accountFirebase.register(email, password, callback)
        }
    }

    fun loginUserAccount(email: String, password: String, callback: (uid: String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.accountFirebase.login(email, password, callback)
        }
    }

    fun logOut() {
        runBlocking {
            userRepository.accountFirebase.logout()
            removeAllUserProfile()
            checkLoginStatus{}
        }
    }

    fun createServerProfile(userName: String, firstName: String, lastName: String, profileImageUrl: String, about: String, callback: (success: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userRepository.getCurrentUser() != null && getUserEmail() != null) {
                val profile = UserProfile(
                    uid = userRepository.getCurrentUser()!!, userName = userName,
                    firstName = firstName, lastName = lastName,
                    email = getUserEmail()!!,
                    profileImageUrl = profileImageUrl, about = about,
                    fcmToken = ""
                )
                userRepository.firebaseDB.insertUser(profile, callback)
            }
        }
    }

    fun saveUserProfile(userProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveUserProfile(userProfile)
        }
    }

    fun removeUserProfile(userProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.removeUserProfile(userProfile)
        }
    }

    fun removeAllUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllList().forEach { profile ->
                userRepository.removeUserProfile(profile)
            }
        }
    }
}