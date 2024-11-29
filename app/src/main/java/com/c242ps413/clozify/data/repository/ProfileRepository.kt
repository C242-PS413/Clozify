package com.c242ps413.clozify.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c242ps413.clozify.data.databases.ClozifyRoomDatabase
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.databases.profile.ProfileDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProfileRepository(application: Application) {
    private val mProfileDao: ProfileDao
    private val executorService: ExecutorService = Executors.newSingleThreadScheduledExecutor()

    init {
        val db = ClozifyRoomDatabase.getDatabase(application)
        mProfileDao = db.profileDao()
    }

    fun getAllProfile(): LiveData<List<Profile>> = mProfileDao.getAllProfile()

    fun getProfileById(id: Int): LiveData<Profile> = mProfileDao.getProfileById(id)

    fun insert(profile: Profile) {
        executorService.execute {
            mProfileDao.insert(profile)
        }
    }

    fun update(profile: Profile) {
        executorService.execute {
            mProfileDao.update(profile)
        }
    }
}
