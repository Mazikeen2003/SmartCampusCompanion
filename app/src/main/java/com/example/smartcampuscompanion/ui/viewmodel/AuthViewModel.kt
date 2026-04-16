package com.example.smartcampuscompanion.ui.viewmodel

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isRegisterSuccess = MutableStateFlow(false)
    val isRegisterSuccess = _isRegisterSuccess.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole = _userRole.asStateFlow()

    private val rememberedUserKey = "remembered_user"

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            fetchUserRole(currentUser.email ?: "")
        }
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sharedPreferences.edit {
                            putString("user_email", email)
                            if (rememberMe) {
                                putString(rememberedUserKey, "$email:$password")
                            } else {
                                remove(rememberedUserKey)
                            }
                        }
                        fetchUserRole(email)
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = task.exception?.message ?: "Login failed"
                    }
                }
        }
    }

    private fun fetchUserRole(email: String) {
        firestore.collection("users").document(email).get()
            .addOnSuccessListener { document ->
                _userRole.value = document.getString("role") ?: "student"
                _isLoading.value = false
                _isSuccess.value = true
            }
            .addOnFailureListener {
                _isLoading.value = false
                _userRole.value = "student"
                _isSuccess.value = true
            }
    }

    fun register(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isRegisterSuccess.value = false
            
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToFirestore(user)
                    } else {
                        _isLoading.value = false
                        _errorMessage.value = task.exception?.message ?: "Registration failed"
                    }
                }
        }
    }

    private fun saveUserToFirestore(user: User) {
        val userData = hashMapOf(
            "email" to user.email,
            "role" to user.role
        )
        firestore.collection("users").document(user.email).set(userData)
            .addOnSuccessListener {
                _isLoading.value = false
                _isRegisterSuccess.value = true
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _errorMessage.value = e.message ?: "Failed to save user data"
            }
    }

    fun resetState() {
        _isRegisterSuccess.value = false
        _isSuccess.value = false
        _errorMessage.value = null
        _isLoading.value = false
    }

    fun getRememberedUser(): User? {
        val remembered = sharedPreferences.getString(rememberedUserKey, null)
        return remembered?.let {
            val parts = it.split(":", limit = 2)
            if (parts.size == 2) User(parts[0], parts[1]) else null
        }
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout() {
        firebaseAuth.signOut()
        _userRole.value = null
        sharedPreferences.edit {
            remove("user_email")
        }
    }
}
