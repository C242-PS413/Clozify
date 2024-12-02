package com.c242ps413.clozify.data.databases.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.c242ps413.clozify.data.model.RecommendationItem

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): LiveData<List<RecommendationItem>>

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getFavoriteItemById(id: String): LiveData<RecommendationItem>
}