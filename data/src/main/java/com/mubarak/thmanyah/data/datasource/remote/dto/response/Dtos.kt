package com.mubarak.thmanyah.data.datasource.remote.dto.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class HomeSectionsResponseDto(
    @SerializedName("sections") val sections: List<SectionDto>,
    @SerializedName("pagination") val pagination: PaginationDto?
)

data class SearchResponseDto(
    @SerializedName("sections") val sections: List<SectionDto>
)

data class SectionDto(
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("order") val order: Any,
    @SerializedName("content") val content: List<JsonElement>
)

data class PaginationDto(
    @SerializedName("next_page") val nextPage: String?,
    @SerializedName("total_pages") val totalPages: Int
)
