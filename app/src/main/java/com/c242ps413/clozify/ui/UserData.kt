package com.c242ps413.clozify.ui

data class UserData(
    var gender: String? = null,
    var season: String? = null,
    var emotion_category: String? = null
)

object UserDataHolder {
    val userData = UserData()
}