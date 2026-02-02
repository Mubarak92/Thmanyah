package com.mubarak.thmanyah.data.model

import com.google.gson.Gson
import com.google.gson.JsonElement

object SectionMapper {

    private val gson = Gson()

    fun mapSections(dtos: List<SectionDto>): List<Section> =
        dtos.map { dto ->
            Section(
                name = dto.name,
                type = SectionType.from(dto.type),
                contentType = ContentType.from(dto.contentType),
                order = when (val o = dto.order) {
                    is Number -> o.toInt()
                    is String -> o.toIntOrNull() ?: 0
                    else -> 0
                },
                items = dto.content.mapNotNull { mapItem(it, dto.contentType) }
            )
        }
            .distinctBy { it.name to it.order }
            .sortedBy { it.order }

    private fun mapItem(json: JsonElement, contentType: String): ContentItem? {
        return try {
            val obj = json.asJsonObject

            val id = obj.getString("podcast_id")
                ?: obj.getString("episode_id")
                ?: obj.getString("audiobook_id")
                ?: obj.getString("article_id")
                ?: return null

            ContentItem(
                id = id,
                name = obj.getString("name") ?: "",
                description = stripHtml(obj.getString("description") ?: ""),
                avatarUrl = obj.getString("avatar_url") ?: "",
                duration = obj.getLong("duration"),
                authorOrPodcastName = obj.getString("author_name")
                    ?: obj.getString("podcast_name")
                    ?: "",
                episodeCount = obj.getIntOrNull("episode_count"),
                releaseDate = obj.getString("release_date")
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun com.google.gson.JsonObject.getString(key: String): String? =
        get(key)?.takeIf { !it.isJsonNull }?.asString

    private fun com.google.gson.JsonObject.getLong(key: String): Long {
        val el = get(key)?.takeIf { !it.isJsonNull && it.isJsonPrimitive } ?: return 0L
        return try { el.asLong } catch (_: Exception) {
            el.asString.toLongOrNull() ?: 0L   // search API returns numbers as strings
        }
    }

    private fun com.google.gson.JsonObject.getIntOrNull(key: String): Int? {
        val el = get(key)?.takeIf { !it.isJsonNull && it.isJsonPrimitive } ?: return null
        return try { el.asInt } catch (_: Exception) {
            el.asString.toIntOrNull()           // search API returns numbers as strings
        }
    }

    private fun stripHtml(html: String): String =
        html.replace(Regex("<[^>]*>"), "").replace("&amp;", "&").trim()
}