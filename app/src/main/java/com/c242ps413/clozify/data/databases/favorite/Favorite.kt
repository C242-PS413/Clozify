package com.c242ps413.clozify.data.databases.favorite

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "imgShirt")
    var imgShirt: String? = null,

    @ColumnInfo(name = "imgPants")
    var imgPants: String? = null,
) : Parcelable