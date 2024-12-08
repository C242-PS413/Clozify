package com.c242ps413.clozify.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.c242ps413.clozify.data.databases.ClozifyRoomDatabase
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.databases.favorite.FavoriteDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ClozifyRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavorites()

    fun insert(favorite: Favorite) {
        executorService.execute {
            mFavoriteDao.insert(favorite)
        }
    }

    fun delete(favorite: Favorite) {
        executorService.execute {
            mFavoriteDao.delete(favorite)
        }
    }

    fun getFavoriteByImage(image: String): LiveData<Favorite> {
        return mFavoriteDao.getFavoriteByImage(image)
    }
}
