package com.c242ps413.clozify.data.databases.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)

    @Query("SELECT * FROM profile")
    fun getAllProfile(): LiveData<List<Profile>>

    @Query("SELECT * FROM profile WHERE id = :id")
    fun getProfileById(id: Int): LiveData<Profile>
}
