package com.c242ps413.clozify.ui.profile.editprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.repository.ProfileRepository

class ProfileUpdateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProfileRepository = ProfileRepository(application)

    // Get Profile by ID
    fun getProfileById(id: Int): LiveData<Profile> = repository.getProfileById(id)

    // Insert Profile
    fun insert(profile: Profile) {
        repository.insert(profile)
    }

    // Get all Profiles
    fun getAllProfile(): LiveData<List<Profile>> {
        return repository.getAllProfile() // Return LiveData without direct observation
    }

    // Update Profile
    fun update(profile: Profile) {
        repository.update(profile)
    }
}
