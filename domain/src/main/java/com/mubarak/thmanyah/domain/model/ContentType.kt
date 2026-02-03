package com.mubarak.thmanyah.domain.model

enum class ContentType {
    PODCAST, EPISODE, AUDIO_BOOK, AUDIO_ARTICLE;

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
