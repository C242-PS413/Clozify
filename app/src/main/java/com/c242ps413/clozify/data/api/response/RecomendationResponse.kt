package com.c242ps413.clozify.data.api.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    @SerializedName("recommendations")
    val recommendations: Recommendations
)

data class Recommendations(
    @SerializedName("TopWear")
    val topWear: List<TopWearItem>,

    @SerializedName("Bottomwear")
    val bottomwear: List<BottomWearItem>,

    @SerializedName("Footwear")
    val footwear: List<FootwearItem>
)

data class TopWearItem(
    @SerializedName("recommendations_item")
    val recommendationsItem: RecommendationItem,

    @SerializedName("more_recommended_items")
    val moreRecommendedItems: List<RecommendationItem>
)

data class BottomWearItem(
    @SerializedName("input_item")
    val inputItem: RecommendationItem,

    @SerializedName("recommended_items")
    val recommendedItems: List<RecommendationItem>
)

data class FootwearItem(
    @SerializedName("recommendations_item")
    val recommendationsItem: RecommendationItem,

    @SerializedName("more_recommended_items")
    val moreRecommendedItems: List<RecommendationItem>
)

data class RecommendationItem(
    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String
)
