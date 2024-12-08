package com.c242ps413.clozify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.FavoriteRepository
import com.c242ps413.clozify.data.repository.ProfileRepository

class HomeViewModel(
    private val profileRepository: ProfileRepository,
    private val favoriteRepository: FavoriteRepository // Menambahkan favoriteRepository jika dibutuhkan
) : ViewModel() {

    val profileData: LiveData<Profile> = profileRepository.getProfileById(1)

    val allFavorites: LiveData<List<Favorite>> = favoriteRepository.getAllFavorites()
}