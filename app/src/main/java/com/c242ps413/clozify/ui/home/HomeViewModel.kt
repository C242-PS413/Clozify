package com.c242ps413.clozify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.ProfileRepository

class HomeViewModel(
    private val profileRepository: ProfileRepository,
    //private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    // Mendapatkan profile berdasarkan ID
    val profileData: LiveData<Profile> = profileRepository.getProfileById(1)
}
