package com.c242ps413.clozify.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c242ps413.clozify.data.repository.FavoriteRepository
import com.c242ps413.clozify.data.repository.ProfileRepository

class HomeViewModelFactory(
    private val profileRepository: ProfileRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(profileRepository, favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}