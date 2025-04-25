package com.example.trackdemics.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackdemics.network.FirebaseAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authService: FirebaseAuthService
) : ViewModel() {

    private val _signUpState = MutableStateFlow<Result<Boolean>?>(null)
    val signUpState: StateFlow<Result<Boolean>?> = _signUpState

    fun signUp(
        email: String,
        password: String,
        role: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            val result = authService.signUpWithEmail(email, password, role, firstName, lastName)
            _signUpState.value = result.map { it != null }
        }
    }

    fun logout() {
        authService.signOut()
    }
    fun clearSignUpState() {
        _signUpState.value = null
    }

}
