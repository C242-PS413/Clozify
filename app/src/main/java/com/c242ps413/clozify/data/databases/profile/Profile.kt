package com.c242ps413.clozify.data.databases.profile

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "profile")
@Parcelize
data class Profile(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 1,

    @ColumnInfo(name = "img_profile")
    var imgProfile: String = "",

    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "gender")
    var gender: String = "",

    @ColumnInfo(name = "location")
    var location: String = ""
) : Parcelable
