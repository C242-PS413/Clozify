package com.c242ps413.clozify.ui.profile.editprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.ProfileRepository

class ProfileUpdateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProfileRepository = ProfileRepository(application)

    fun getProfileById(id: Int): LiveData<Profile> = repository.getProfileById(id)

    fun insert(profile: Profile) {
        repository.insert(profile)
    }

    fun getAllProfile(): LiveData<List<Profile>> {
        return repository.getAllProfile()
    }

    fun update(profile: Profile) {
        repository.update(profile)
    }
}
