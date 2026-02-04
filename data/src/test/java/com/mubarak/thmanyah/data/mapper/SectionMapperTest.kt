package com.mubarak.thmanyah.data.mapper

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.mubarak.thmanyah.data.datasource.remote.dto.response.HomeSectionsResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.PaginationDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SearchResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SectionDto
import com.mubarak.thmanyah.domain.model.ContentType
import com.mubarak.thmanyah.domain.model.SectionType
import org.junit.Before
import org.junit.Test

class SectionMapperTest {

    private lateinit var mapper: SectionMapper
    private val gson = Gson()

    @Before
    fun setup() {
        mapper = SectionMapper()
    }


    @Test
    fun `mapHomeSections returns correct pagination info`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createPodcastSectionDto()),
            pagination = PaginationDto(nextPage = "/home_sections?page=2", totalPages = 5)
        )

        // When
        val result = mapper.mapHomeSections(dto, currentPage = 1)

        // Then
        assertThat(result.currentPage).isEqualTo(1)
        assertThat(result.totalPages).isEqualTo(5)
        assertThat(result.nextPage).isEqualTo(2)
        assertThat(result.hasMorePages).isTrue()
    }

    @Test
    fun `mapHomeSections with null pagination returns defaults`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createPodcastSectionDto()),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, currentPage = 1)

        // Then
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.nextPage).isNull()
        assertThat(result.hasMorePages).isFalse()
    }

    @Test
    fun `mapHomeSections on last page has no next page`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createPodcastSectionDto()),
            pagination = PaginationDto(nextPage = null, totalPages = 3)
        )

        // When
        val result = mapper.mapHomeSections(dto, currentPage = 3)

        // Then
        assertThat(result.nextPage).isNull()
        assertThat(result.hasMorePages).isFalse()
    }


    @Test
    fun `mapSections correctly maps section type SQUARE`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(type = "square")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().type).isEqualTo(SectionType.SQUARE)
    }

    @Test
    fun `mapSections correctly maps section type BIG_SQUARE`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(type = "big_square")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().type).isEqualTo(SectionType.BIG_SQUARE)
    }

    @Test
    fun `mapSections correctly maps section type TWO_LINES_GRID`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(type = "2_lines_grid")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().type).isEqualTo(SectionType.TWO_LINES_GRID)
    }

    @Test
    fun `mapSections correctly maps section type QUEUE`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(type = "queue")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().type).isEqualTo(SectionType.QUEUE)
    }

    @Test
    fun `mapSections defaults unknown type to SQUARE`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(type = "unknown_type")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().type).isEqualTo(SectionType.SQUARE)
    }


    @Test
    fun `mapSections correctly maps content type PODCAST`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(contentType = "podcast")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().contentType).isEqualTo(ContentType.PODCAST)
    }

    @Test
    fun `mapSections correctly maps content type EPISODE`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(contentType = "episode")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().contentType).isEqualTo(ContentType.EPISODE)
    }

    @Test
    fun `mapSections correctly maps content type AUDIO_BOOK`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(contentType = "audio_book")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().contentType).isEqualTo(ContentType.AUDIO_BOOK)
    }

    // ==================== Content Item Mapping Tests ====================

    @Test
    fun `mapContentItem correctly maps podcast`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createPodcastSectionDto()),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)
        val item = result.data.first().items.first()

        // Then
        assertThat(item.id).isEqualTo("123")
        assertThat(item.name).isEqualTo("Test Podcast")
        assertThat(item.description).isEqualTo("A great podcast")
        assertThat(item.imageUrl).isEqualTo("https://example.com/image.jpg")
        assertThat(item.durationSeconds).isEqualTo(3600)
        assertThat(item.episodeCount).isEqualTo(100)
    }

    @Test
    fun `mapContentItem correctly maps episode`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createEpisodeSectionDto()),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)
        val item = result.data.first().items.first()

        // Then
        assertThat(item.id).isEqualTo("ep_456")
        assertThat(item.name).isEqualTo("Episode Title")
        assertThat(item.authorOrPodcastName).isEqualTo("Parent Podcast")
    }

    @Test
    fun `mapContentItem strips HTML from description`() {
        // Given
        val contentJson = """
            {
                "podcast_id": "123",
                "name": "Test",
                "description": "<p>Hello <strong>World</strong></p>",
                "avatar_url": "https://example.com/img.jpg",
                "duration": 100
            }
        """.trimIndent()

        val dto = HomeSectionsResponseDto(
            sections = listOf(
                SectionDto(
                    name = "Test",
                    type = "square",
                    contentType = "podcast",
                    order = 1,
                    content = listOf(JsonParser.parseString(contentJson))
                )
            ),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)
        val item = result.data.first().items.first()

        // Then
        assertThat(item.description).isEqualTo("Hello World")
    }

    @Test
    fun `mapContentItem handles missing optional fields`() {
        // Given
        val contentJson = """
            {
                "podcast_id": "123",
                "name": "Test",
                "description": "",
                "avatar_url": "",
                "duration": 0
            }
        """.trimIndent()

        val dto = HomeSectionsResponseDto(
            sections = listOf(
                SectionDto(
                    name = "Test",
                    type = "square",
                    contentType = "podcast",
                    order = 1,
                    content = listOf(JsonParser.parseString(contentJson))
                )
            ),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)
        val item = result.data.first().items.first()

        // Then
        assertThat(item.episodeCount).isNull()
        assertThat(item.releaseDate).isNull()
        assertThat(item.authorOrPodcastName).isEmpty()
    }

    @Test
    fun `mapContentItem skips items without valid ID`() {
        // Given
        val contentJson = """
            {
                "name": "No ID Item",
                "description": "This has no ID",
                "avatar_url": "https://example.com/img.jpg",
                "duration": 100
            }
        """.trimIndent()

        val dto = HomeSectionsResponseDto(
            sections = listOf(
                SectionDto(
                    name = "Test",
                    type = "square",
                    contentType = "podcast",
                    order = 1,
                    content = listOf(JsonParser.parseString(contentJson))
                )
            ),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().items).isEmpty()
    }

    // ==================== Search Results Tests ====================

    @Test
    fun `mapSearchResults correctly maps sections`() {
        // Given
        val dto = SearchResponseDto(
            sections = listOf(createPodcastSectionDto(), createEpisodeSectionDto())
        )

        // When
        val result = mapper.mapSearchResults(dto)

        // Then
        assertThat(result).hasSize(2)
    }

    // ==================== Order Parsing Tests ====================

    @Test
    fun `sections are sorted by order`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(
                createSectionDto(name = "Third", order = 3),
                createSectionDto(name = "First", order = 1),
                createSectionDto(name = "Second", order = 2)
            ),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.map { it.name }).containsExactly("First", "Second", "Third").inOrder()
    }

    @Test
    fun `order handles string numbers`() {
        // Given
        val dto = HomeSectionsResponseDto(
            sections = listOf(createSectionDto(order = "5")),
            pagination = null
        )

        // When
        val result = mapper.mapHomeSections(dto, 1)

        // Then
        assertThat(result.data.first().order).isEqualTo(5)
    }

    // ==================== Helper Methods ====================

    private fun createSectionDto(
        name: String = "Test Section",
        type: String = "square",
        contentType: String = "podcast",
        order: Any = 1
    ) = SectionDto(
        name = name,
        type = type,
        contentType = contentType,
        order = order,
        content = listOf(
            JsonParser.parseString("""
                {
                    "podcast_id": "123",
                    "name": "Test",
                    "description": "Desc",
                    "avatar_url": "https://example.com/img.jpg",
                    "duration": 100
                }
            """.trimIndent())
        )
    )

    private fun createPodcastSectionDto() = SectionDto(
        name = "Top Podcasts",
        type = "square",
        contentType = "podcast",
        order = 1,
        content = listOf(
            JsonParser.parseString("""
                {
                    "podcast_id": "123",
                    "name": "Test Podcast",
                    "description": "A great podcast",
                    "avatar_url": "https://example.com/image.jpg",
                    "duration": 3600,
                    "episode_count": 100
                }
            """.trimIndent())
        )
    )

    private fun createEpisodeSectionDto() = SectionDto(
        name = "Latest Episodes",
        type = "2_lines_grid",
        contentType = "episode",
        order = 2,
        content = listOf(
            JsonParser.parseString("""
                {
                    "episode_id": "ep_456",
                    "name": "Episode Title",
                    "description": "Episode description",
                    "avatar_url": "https://example.com/ep.jpg",
                    "duration": 1800,
                    "podcast_name": "Parent Podcast",
                    "release_date": "2024-01-15"
                }
            """.trimIndent())
        )
    )
}
