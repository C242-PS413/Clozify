package com.c242ps413.clozify.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllFavorites(): LiveData<List<Favorite>> {
        _isLoading.value = true
        val events = mFavoriteRepository.getAllFavorites()
        return events
    }

    fun getFavoriteByImage(image: String): LiveData<Favorite> = mFavoriteRepository.getFavoriteByImage(image)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
}

