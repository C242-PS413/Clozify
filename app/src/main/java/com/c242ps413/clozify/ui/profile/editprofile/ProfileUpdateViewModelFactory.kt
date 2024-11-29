package com.c242ps413.clozify.ui.profile.editprofile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileUpdateViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileUpdateViewModel::class.java)) {
            return ProfileUpdateViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}