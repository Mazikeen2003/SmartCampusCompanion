package com.example.smartcampuscompanion.ui.Screens

import androidx.lifecycle.ViewModel

// Data class to hold user information
data class User(val email: String, val pass: String)

class AuthViewModel : ViewModel() {
    // In-memory list to store registered users
    private val users = mutableListOf<User>()

    /**
     * Registers a new user.
     * @param user The user to register.
     * @return True if registration is successful, false if the user already exists.
     */
    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) {
            return false // User already exists
        }
        users.add(user)
        return true
    }

    /**
     * Logs in a user.
     * @param user The user credentials to check.
     * @return True if login is successful, false otherwise.
     */
    fun login(user: User): Boolean {
        return users.any { it.email == user.email && it.pass == user.pass }
    }
}