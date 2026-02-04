package com.mubarak.thmanyah.data.mapper

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mubarak.thmanyah.data.datasource.remote.dto.response.HomeSectionsResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SearchResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SectionDto
import com.mubarak.thmanyah.domain.model.*
import java.util.UUID
import javax.inject.Inject

class SectionMapper @Inject constructor() {

    fun mapHomeSections(dto: HomeSectionsResponseDto, currentPage: Int): PaginatedSections {
        val sections = mapSections(dto.sections)
        val totalPages = dto.pagination?.totalPages ?: 1
        val nextPage = dto.pagination?.nextPage?.let { extractPage(it) }
        return PaginatedSections(sections, currentPage, totalPages, nextPage, nextPage != null)
    }

    fun mapSearchResults(dto: SearchResponseDto): List<Section> = mapSections(dto.sections)

    private fun mapSections(dtos: List<SectionDto>): List<Section> = dtos.mapIndexed { index, dto ->
        Section(
            id = "${dto.name}_${parseOrder(dto.order)}_${index}_${UUID.randomUUID().toString().take(8)}",
            name = dto.name,
            type = SectionType.from(dto.type),
            contentType = ContentType.from(dto.contentType),
            order = parseOrder(dto.order),
            items = dto.content.mapNotNull { mapContentItem(it) }
        )
    }.sortedBy { it.order }

    private fun mapContentItem(json: JsonElement): ContentItem? = try {
        val obj = json.asJsonObject
        val id = obj.getString("podcast_id") ?: obj.getString("episode_id")
        ?: obj.getString("audiobook_id") ?: obj.getString("article_id") ?: return null
        ContentItem(
            id = id,
            name = obj.getString("name").orEmpty(),
            description = stripHtml(obj.getString("description").orEmpty()),
            imageUrl = obj.getString("avatar_url").orEmpty(),
            durationSeconds = obj.getNumberAsLong("duration"),
            authorOrPodcastName = obj.getString("author_name") ?: obj.getString("podcast_name") ?: "",
            episodeCount = obj.getNumberAsInt("episode_count"),
            releaseDate = obj.getString("release_date")
        )
    } catch (e: Exception) { null }

    private fun JsonObject.getString(key: String): String? = get(key)?.takeIf { !it.isJsonNull }?.asString
    private fun JsonObject.getNumberAsLong(key: String): Long {
        val el = get(key)?.takeIf { !it.isJsonNull && it.isJsonPrimitive } ?: return 0L
        return try { el.asLong } catch (e: Exception) { el.asString.toLongOrNull() ?: 0L }
    }
    private fun JsonObject.getNumberAsInt(key: String): Int? {
        val el = get(key)?.takeIf { !it.isJsonNull && it.isJsonPrimitive } ?: return null
        return try { el.asInt } catch (e: Exception) { el.asString.toIntOrNull() }
    }
    private fun parseOrder(order: Any): Int = when (order) {
        is Number -> order.toInt()
        is String -> order.toIntOrNull() ?: 0
        else -> 0
    }
    private fun extractPage(path: String): Int? = Regex("page=(\\d+)").find(path)?.groupValues?.get(1)?.toIntOrNull()
    private fun stripHtml(html: String): String = html.replace(Regex("<[^>]*>"), "").replace("&amp;", "&").trim()
}
