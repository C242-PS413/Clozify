package com.c242ps413.clozify.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c242ps413.clozify.data.databases.ClozifyRoomDatabase
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.databases.favorite.FavoriteDao
import com.c242ps413.clozify.data.model.RecommendationItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ClozifyRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<RecommendationItem>> = mFavoriteDao.getAllFavorites()

    fun getFavoriteItemById(id: String): LiveData<RecommendationItem> = mFavoriteDao.getFavoriteItemById(id)

    fun insert(favorite: Favorite) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: Favorite) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

    fun update(favorite: Favorite) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }
}