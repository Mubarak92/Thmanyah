package com.mubarak.thmanyah.data.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class HomeSectionsResponse(
    @SerializedName("sections") val sections: List<SectionDto>,
    @SerializedName("pagination") val pagination: PaginationDto?
)

data class SearchResponse(
    @SerializedName("sections") val sections: List<SectionDto>
)

data class PaginationDto(
    @SerializedName("next_page") val nextPage: String?,
    @SerializedName("total_pages") val totalPages: Int
)

data class SectionDto(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("order") val order: Any,          // API returns Int for home, String for search
    @SerializedName("content") val content: List<JsonElement>
)