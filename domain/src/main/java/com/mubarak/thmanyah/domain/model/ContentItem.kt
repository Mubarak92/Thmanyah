package com.mubarak.thmanyah.domain.model

data class ContentItem(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val durationSeconds: Long,
    val authorOrPodcastName: String,
    val episodeCount: Int? = null,
    val releaseDate: String? = null
) {
    val formattedDuration: String get() {
        val hours = durationSeconds / 3600
        val minutes = (durationSeconds % 3600) / 60
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes} min"
            else -> "< 1 min"
        }
    }
}
