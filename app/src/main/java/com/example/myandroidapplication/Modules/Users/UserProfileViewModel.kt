package com.example.myandroidapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myandroidapplication.Model.User
import com.example.myandroidapplication.repository.UserRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _userProfile.value = userRepository.getCurrentUser()
        }
    }

    fun updateUserProfile(name: String, imageUri: String) {
        viewModelScope.launch {
            val currentUser = _userProfile.value
            if (currentUser != null) {
                userRepository.updateUserProfile(currentUser, name, imageUri)
                _userProfile.value = currentUser
            }
        }
    }
}
