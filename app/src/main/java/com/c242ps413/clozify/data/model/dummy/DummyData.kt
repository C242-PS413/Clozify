package com.c242ps413.clozify.data.model.dummy

import com.c242ps413.clozify.R
import com.c242ps413.clozify.data.model.RecommendationItem

object DummyData {
    val recommendations = listOf(
        RecommendationItem("Casual Look 1", R.drawable.dummybaju, R.drawable.dummycelanaa),
        RecommendationItem("Formal Look 2", R.drawable.dummybaju2, R.drawable.dummycelana2),
        RecommendationItem("Sporty Style 3", R.drawable.dummybaju3, R.drawable.dummycelana3)
    )
}
