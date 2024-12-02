package com.c242ps413.clozify.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.c242ps413.clozify.data.databases.favorite.Favorite
import com.c242ps413.clozify.data.databases.favorite.FavoriteDao
import com.c242ps413.clozify.data.databases.profile.Profile
import com.c242ps413.clozify.data.databases.profile.ProfileDao

@Database(entities = [Profile::class, Favorite::class], version = 2)
abstract class ClozifyRoomDatabase : RoomDatabase() {
    abstract fun profileDao() : ProfileDao
    abstract fun favoriteDao() : FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: ClozifyRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ClozifyRoomDatabase {
            return INSTANCE ?: synchronized(ClozifyRoomDatabase::class.java) {
                INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                    ClozifyRoomDatabase::class.java, "Clozify")
                    .build().also { INSTANCE = it }
            }
        }
    }
}