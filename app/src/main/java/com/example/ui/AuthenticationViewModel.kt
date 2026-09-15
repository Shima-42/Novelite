package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.NoveliteLogger
import com.example.data.NoveliteRepository
import com.example.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
  val repository: NoveliteRepository = NoveliteRepository()
) : ViewModel() {

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

  val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
  val currentUser: StateFlow<UserProfile> = repository.currentUser

  private val reservedUsernames = setOf("aurora_reads", "storyteller_alex", "novel_wizard", "admin")

  fun validatePasswordStrength(password: String): String? {
    if (password.length < 6) {
      return "Password must be at least 6 characters long."
    }
    if (!password.any { it.isDigit() } && !password.any { !it.isLetterOrDigit() }) {
      return "Password must contain at least one number or special character."
    }
    return null
  }

  fun validateUsernameUnique(username: String): Boolean {
    return !reservedUsernames.contains(username.trim().lowercase())
  }

  fun login(userOrEmail: String, pass: String, onSuccess: () -> Unit = {}) {
    try {
      _isLoading.value = true
      _errorMessage.value = null
      repository.login(userOrEmail, pass)
      onSuccess()
    } catch (e: Exception) {
      _errorMessage.value = e.message ?: "An unexpected error occurred during login."
    } finally {
      _isLoading.value = false
    }

    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("AuthenticationViewModel: login initiated for $userOrEmail")
      try {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
          if (userOrEmail.contains("@")) userOrEmail else "$userOrEmail@novelite.app",
          pass
        )
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth signIn notice: ${e.message}")
      }
    }
  }

  fun signup(username: String, email: String, pass: String, onSuccess: () -> Unit = {}) {
    if (!validateUsernameUnique(username)) {
      _errorMessage.value = "Username '$username' is already taken. Please choose another username."
      return
    }
    val pwdError = validatePasswordStrength(pass)
    if (pwdError != null) {
      _errorMessage.value = pwdError
      return
    }

    try {
      _isLoading.value = true
      _errorMessage.value = null
      repository.signup(username, email, pass)
      onSuccess()
    } catch (e: Exception) {
      _errorMessage.value = e.message ?: "An unexpected error occurred during signup."
    } finally {
      _isLoading.value = false
    }

    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("AuthenticationViewModel: signup initiated for $username ($email)")
      try {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth createUser notice: ${e.message}")
      }
    }
  }

  fun signOut() {
    try {
      _isLoading.value = true
      repository.logout()
    } catch (e: Exception) {
      _errorMessage.value = e.message
    } finally {
      _isLoading.value = false
    }

    viewModelScope.launch(Dispatchers.IO) {
      NoveliteLogger.logRecomposition("AuthenticationViewModel: signOut initiated")
      try {
        FirebaseAuth.getInstance().signOut()
      } catch (e: Throwable) {
        NoveliteLogger.logRecomposition("FirebaseAuth signOut notice: ${e.message}")
      }
    }
  }
}
