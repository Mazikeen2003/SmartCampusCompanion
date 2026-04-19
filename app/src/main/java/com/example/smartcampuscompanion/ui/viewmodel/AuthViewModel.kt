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
import kotlinx.coroutines.tasks.await // Importante ito para sa .await()
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
            refreshUserRole()
        }
    }

    fun refreshUserRole() {
        val currentUser = firebaseAuth.currentUser ?: return
        viewModelScope.launch {
            try {
                val email = currentUser.email ?: return@launch
                val document = firestore.collection("users").document(email).get().await()
                val role = document.getString("role") ?: "student"
                _userRole.value = role
            } catch (e: Exception) {
                _userRole.value = "student"
            }
        }
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. Firebase Auth Sign In
                firebaseAuth.signInWithEmailAndPassword(email, password).await()

                // 2. Pag-save sa SharedPreferences
                sharedPreferences.edit {
                    putString("user_email", email)
                    if (rememberMe) {
                        putString(rememberedUserKey, "$email:$password")
                    } else {
                        remove(rememberedUserKey)
                    }
                }

                // 3. Fetch Role bago sabihing Success
                val document = firestore.collection("users").document(email).get().await()
                _userRole.value = document.getString("role") ?: "student"
                _isSuccess.value = true

            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage ?: "Login failed"
            }
        }
    }

    private fun fetchUserRole(email: String) {
        viewModelScope.launch {
            try {
                val document = firestore.collection("users").document(email).get().await()
                val role = document.getString("role") ?: "student"

                _userRole.value = role
                _isSuccess.value = true
            } catch (e: Exception) {
                // Default to student if firestore fails but auth is successful
                _userRole.value = "student"
                _isSuccess.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. Create User sa Auth
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()

                // 2. Save User details at Role sa Firestore
                saveUserToFirestore(user)

            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = e.localizedMessage ?: "Registration failed"
            }
        }
    }

    private suspend fun saveUserToFirestore(user: User) {
        val userData = hashMapOf(
            "email" to user.email,
            "role" to user.role // Dito nase-save kung 'admin' o 'student'
        )
        try {
            firestore.collection("users").document(user.email).set(userData).await()
            _isRegisterSuccess.value = true
        } catch (e: Exception) {
            _errorMessage.value = "Auth created, but failed to save profile: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun resetState() {
        _isRegisterSuccess.value = false
        _isSuccess.value = false
        _errorMessage.value = null
        _isLoading.value = false
    }

    // Kinonvert ko ito para mas safe ang parsing
    fun getRememberedUser(): User? {
        val remembered = sharedPreferences.getString(rememberedUserKey, null) ?: return null
        val parts = remembered.split(":", limit = 2)
        return if (parts.size == 2) User(email = parts[0], password = parts[1]) else null
    }

    fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun logout() {
        firebaseAuth.signOut()
        _userRole.value = null
        _isSuccess.value = false
        sharedPreferences.edit {
            remove("user_email")
            // Note: Hindi natin tinatanggal ang remembered_user para sa "Remember Me" feature
        }
    }
}   