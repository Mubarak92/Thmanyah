package com.mubarak.thmanyah.data.model


enum class SectionType {
    SQUARE,
    BIG_SQUARE,
    TWO_LINES_GRID,
    QUEUE;

    companion object {
        fun from(raw: String): SectionType = when (raw.lowercase().trim()) {
            "square" -> SQUARE
            "big_square", "big square" -> BIG_SQUARE
            "2_lines_grid" -> TWO_LINES_GRID
            "queue" -> QUEUE
            else -> SQUARE
        }
    }
}


enum class ContentType {
    PODCAST,
    EPISODE,
    AUDIO_BOOK,
    AUDIO_ARTICLE;

    companion object {
        fun from(raw: String): ContentType = when (raw.lowercase().trim()) {
            "podcast" -> PODCAST
            "episode" -> EPISODE
            "audio_book" -> AUDIO_BOOK
            "audio_article" -> AUDIO_ARTICLE
            else -> PODCAST
        }
    }
}

data class Section(
    val name: String,
    val type: SectionType,
    val contentType: ContentType,
    val order: Int,
    val items: List<ContentItem>
)

data class ContentItem(
    val id: String,
    val name: String,
    val description: String,
    val avatarUrl: String,
    val duration: Long,
    val authorOrPodcastName: String,   // author_name for books/articles, podcast_name for episodes
    val episodeCount: Int? = null,     // podcasts only
    val releaseDate: String? = null
)