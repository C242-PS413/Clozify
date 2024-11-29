package com.c242ps413.clozify.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.ProfileRepository

class ProfileViewModel(application: Application) : ViewModel() {
    private val mProfileRepository: ProfileRepository = ProfileRepository(application)

    fun getAllProfile(): LiveData<List<Profile>> {
        val profile = mProfileRepository.getAllProfile()
        profile.observeForever {

        }
        return profile
    }
}
