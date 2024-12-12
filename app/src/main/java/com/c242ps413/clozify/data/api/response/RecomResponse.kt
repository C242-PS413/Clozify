package com.c242ps413.clozify.data.api.response

import com.google.gson.annotations.SerializedName

data class RecomResponse(
	@field:SerializedName("Bottomwear")
	val bottomwear: Bottomwear? = null,

	@field:SerializedName("Footwear")
	val footwear: Footwear? = null,

	@field:SerializedName("TopWear")
	val topWear: TopWear? = null
)

data class Footwear(
	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class Bottomwear(
	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class TopWear(
	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null
)

data class RecommendationsItem(
	@field:SerializedName("more_recommended_items")
	val moreRecommendedItems: List<MoreRecommendedItemsItem?>? = null,

	@field:SerializedName("recommendations_item")
	val recommendationsItem: RecommendationsItem? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)

data class MoreRecommendedItemsItem(
	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)
