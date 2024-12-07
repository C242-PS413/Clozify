package com.c242ps413.clozify.data.model.dummy

import com.c242ps413.clozify.data.api.response.BottomWearItem
import com.c242ps413.clozify.data.api.response.FootwearItem
import com.c242ps413.clozify.data.api.response.RecommendationItem
import com.c242ps413.clozify.data.api.response.Recommendations
import com.c242ps413.clozify.data.api.response.TopWearItem

object DummyData {

    val recommendations = Recommendations(
        topWear = listOf(
            TopWearItem(
                recommendationsItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/baju",  // Gambar T-shirt dari drawable
                    name = "Baju"
                ),
                moreRecommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummybaju2",  // Gambar Hoodie dari drawable
                        name = "Blouse"
                    ),
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummybaju3",  // Gambar Jacket dari drawable
                        name = "Jacket"
                    )
                )
            ),
            TopWearItem(
                recommendationsItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/baju",  // Gambar Polo Shirt
                    name = "Baju"
                ),
                moreRecommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummybaju",  // Gambar Sweater
                        name = "Sweater"
                    )
                )
            )
        ),
        bottomwear = listOf(
            BottomWearItem(
                inputItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/celana",  // Gambar Jeans
                    name = "Celana"
                ),
                recommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummycelana2",  // Gambar Shorts
                        name = "Skirt"
                    ),
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummycelana3",  // Gambar Chinos
                        name = "Jeans"
                    )
                )
            ),
            BottomWearItem(
                inputItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/celana",  // Gambar Leggings
                    name = "Celana"
                ),
                recommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummycelanaa",  // Gambar Cargo Pants
                        name = "Cloth Pants"
                    )
                )
            )
        ),
        footwear = listOf(
            FootwearItem(
                recommendationsItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/sepatu",  // Gambar Sneakers
                    name = "Sepatu"
                ),
                moreRecommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummysepatuu",  // Gambar Boots
                        name = "Boots"
                    ),
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummysepatu2",  // Gambar Slippers
                        name = "High Heels"
                    )
                )
            ),
            FootwearItem(
                recommendationsItem = RecommendationItem(
                    image = "android.resource://com.c242ps413.clozify/drawable/sepatu",  // Gambar Sandals
                    name = "Sepatu"
                ),
                moreRecommendedItems = listOf(
                    RecommendationItem(
                        image = "android.resource://com.c242ps413.clozify/drawable/dummysepatu3",  // Gambar Loafers
                        name = "Sneakers"
                    )
                )
            )
        )
    )
}
