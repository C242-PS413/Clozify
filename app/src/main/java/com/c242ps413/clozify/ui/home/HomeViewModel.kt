package com.c242ps413.clozify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.ProfileRepository

class HomeViewModel(private val repository: ProfileRepository) : ViewModel() {

    val profileData: LiveData<Profile> = repository.getProfileById(1)
}
