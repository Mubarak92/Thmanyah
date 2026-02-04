package com.mubarak.thmanyah.core.testing.fake

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.model.*
import com.mubarak.thmanyah.domain.repository.SearchRepository

class FakeSearchRepository : SearchRepository {

    var shouldReturnError = false
    var errorMessage = "Search failed"
    var delayMs: Long = 0
    var searchResults: List<Section> = createFakeSearchResults()

    private var searchCallCount = 0
    val searchQueries = mutableListOf<String>()

    override suspend fun search(query: String): Resource<List<Section>> {
        searchCallCount++
        searchQueries.add(query)

        if (delayMs > 0) {
            kotlinx.coroutines.delay(delayMs)
        }

        return if (shouldReturnError) {
            Resource.error(Exception(errorMessage))
        } else {
            Resource.success(searchResults.filter { section ->
                section.items.any { it.name.contains(query, ignoreCase = true) }
            }.ifEmpty { searchResults })
        }
    }

    fun getSearchCallCount() = searchCallCount

    fun reset() {
        shouldReturnError = false
        errorMessage = "Search failed"
        delayMs = 0
        searchCallCount = 0
        searchQueries.clear()
        searchResults = createFakeSearchResults()
    }

    companion object {
        fun createFakeSearchResults(): List<Section> = listOf(
            Section(
                id = "search_1",
                name = "Search Results",
                type = SectionType.SQUARE,
                contentType = ContentType.PODCAST,
                order = 1,
                items = listOf(
                    ContentItem(
                        id = "podcast_1",
                        name = "Tech Talk",
                        description = "A podcast about technology",
                        imageUrl = "https://example.com/tech.jpg",
                        durationSeconds = 3600,
                        authorOrPodcastName = "Tech Host",
                        episodeCount = 100
                    ),
                    ContentItem(
                        id = "podcast_2",
                        name = "Science Daily",
                        description = "Daily science news",
                        imageUrl = "https://example.com/science.jpg",
                        durationSeconds = 1800,
                        authorOrPodcastName = "Science Team",
                        episodeCount = 50
                    )
                )
            )
        )
    }
}
