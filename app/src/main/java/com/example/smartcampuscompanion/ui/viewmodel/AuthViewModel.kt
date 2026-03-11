package com.example.smartcampuscompanion.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.example.smartcampuscompanion.data.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    private val users = mutableListOf<User>()
    private val registeredUsersKey = "registered_users"
    private val rememberedUserKey = "remembered_user"

    init {
        // Add hardcoded user
        users.add(User("admin", "admin123"))
        loadRegisteredUsers()
    }

    private fun loadRegisteredUsers() {
        val savedUsers = sharedPreferences.getStringSet(registeredUsersKey, emptySet()) ?: emptySet()
        savedUsers.forEach {
            val parts = it.split(":", limit = 2)
            if (parts.size == 2) {
                users.add(User(parts[0], parts[1]))
            }
        }
    }

    private fun saveRegisteredUsers() {
        val userSet = users.map { "${it.email}:${it.password}" }.toSet()
        sharedPreferences.edit {
            putStringSet(registeredUsersKey, userSet)
        }
    }

    fun login(user: User, rememberMe: Boolean): Boolean {
        val loggedIn = users.any { it.email == user.email && it.password == user.password }
        if (loggedIn) {
            sharedPreferences.edit {
                putString("user_email", user.email)
                if (rememberMe) {
                    putString(rememberedUserKey, "${user.email}:${user.password}")
                } else {
                    remove(rememberedUserKey)
                }
            }
        }
        return loggedIn
    }

    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) {
            return false // User with this email already exists
        }
        users.add(user)
        saveRegisteredUsers()
        return true
    }

    fun getRememberedUser(): User? {
        val remembered = sharedPreferences.getString(rememberedUserKey, null)
        return remembered?.let {
            val parts = it.split(":", limit = 2)
            if (parts.size == 2) User(parts[0], parts[1]) else null
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("user_email")
    }

    fun logout() {
        sharedPreferences.edit {
            remove("user_email")
        }
    }
}
