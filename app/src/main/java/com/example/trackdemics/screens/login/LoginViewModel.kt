package com.example.trackdemics.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackdemics.network.FirebaseAuthService
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: FirebaseAuthService
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState: StateFlow<Result<Boolean>?> = _loginState

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch {
            val result = authService.signInWithEmail(email, password)
            val firestore = FirebaseFirestore.getInstance()
            val normalizedEmail = email.trim().lowercase()

            if (result.isSuccess) {
                // 🔍 Check if the user is registered
                val collection = when (role.uppercase()) {
                    "STUDENT" -> "students"
                    "PROFESSOR" -> "professors"
                    "ADMIN" -> "professors" // If admin logic is same
                    else -> {
                        _loginState.value = Result.failure(Exception("Invalid role"))
                        return@launch
                    }
                }

                val snapshot = firestore.collection(collection)
                    .whereEqualTo("email", normalizedEmail)
                    .get()
                    .await()

                val doc = snapshot.documents.firstOrNull()
                val isRegistered = doc?.getBoolean("registered") ?: false

                if (!isRegistered) {
                    _loginState.value = Result.failure(Exception("Account not registered yet. Please sign up."))
                    return@launch
                }

                _loginState.value = Result.success(true)
            } else {
                _loginState.value = Result.failure(result.exceptionOrNull()!!)
            }
        }
    }

    fun clearLoginState() {
        _loginState.value = null
    }
}
