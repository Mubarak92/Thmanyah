package com.mubarak.thmanyah.core.testing.fake

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.model.*
import com.mubarak.thmanyah.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {

    var shouldReturnError = false
    var errorMessage = "Failed to load sections"
    var delayMs: Long = 0
    var totalPages = 3
    var sectionsPerPage = 2

    private var loadCallCount = 0
    private val loadedPages = mutableListOf<Int>()

    override suspend fun getHomeSections(page: Int): Resource<PaginatedSections> {
        loadCallCount++
        loadedPages.add(page)

        if (delayMs > 0) {
            kotlinx.coroutines.delay(delayMs)
        }

        return if (shouldReturnError) {
            Resource.error(Exception(errorMessage))
        } else {
            val sections = createFakeSections(page)
            val hasMore = page < totalPages
            Resource.success(
                PaginatedSections(
                    data = sections,
                    currentPage = page,
                    totalPages = totalPages,
                    nextPage = if (hasMore) page + 1 else null,
                    hasMorePages = hasMore
                )
            )
        }
    }

    fun getLoadCallCount() = loadCallCount
    fun getLoadedPages(): List<Int> = loadedPages.toList()

    fun reset() {
        shouldReturnError = false
        errorMessage = "Failed to load sections"
        delayMs = 0
        totalPages = 3
        sectionsPerPage = 2
        loadCallCount = 0
        loadedPages.clear()
    }

    private fun createFakeSections(page: Int): List<Section> = (1..sectionsPerPage).map { index ->
        val sectionNum = (page - 1) * sectionsPerPage + index
        Section(
            id = "section_$sectionNum",
            name = "Section $sectionNum",
            type = when (sectionNum % 4) {
                0 -> SectionType.QUEUE
                1 -> SectionType.SQUARE
                2 -> SectionType.BIG_SQUARE
                else -> SectionType.TWO_LINES_GRID
            },
            contentType = ContentType.PODCAST,
            order = sectionNum,
            items = createFakeItems(sectionNum)
        )
    }

    companion object {
        fun createFakeItems(sectionNum: Int): List<ContentItem> = (1..5).map { index ->
            ContentItem(
                id = "item_${sectionNum}_$index",
                name = "Item $index in Section $sectionNum",
                description = "Description for item $index",
                imageUrl = "https://example.com/image_${sectionNum}_$index.jpg",
                durationSeconds = 3600L * index,
                authorOrPodcastName = "Author $index",
                episodeCount = 10 * index,
                releaseDate = "2024-01-0$index"
            )
        }

        fun createSingleSection(): Section = Section(
            id = "test_section",
            name = "Test Section",
            type = SectionType.SQUARE,
            contentType = ContentType.PODCAST,
            order = 1,
            items = listOf(
                ContentItem(
                    id = "test_item",
                    name = "Test Podcast",
                    description = "Test Description",
                    imageUrl = "https://example.com/test.jpg",
                    durationSeconds = 3600,
                    authorOrPodcastName = "Test Author",
                    episodeCount = 100
                )
            )
        )
    }
}
