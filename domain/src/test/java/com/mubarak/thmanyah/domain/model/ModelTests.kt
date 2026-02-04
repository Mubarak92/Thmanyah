package com.mubarak.thmanyah.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ContentItemTest {

    @Test
    fun `formattedDuration formats hours and minutes`() {
        val item = createContentItem(durationSeconds = 5400) // 1h 30m

        assertThat(item.formattedDuration).isEqualTo("1h 30m")
    }

    @Test
    fun `formattedDuration formats hours only when no minutes`() {
        val item = createContentItem(durationSeconds = 7200) // 2h 0m

        assertThat(item.formattedDuration).isEqualTo("2h 0m")
    }

    @Test
    fun `formattedDuration formats minutes only`() {
        val item = createContentItem(durationSeconds = 1800) // 30m

        assertThat(item.formattedDuration).isEqualTo("30 min")
    }

    @Test
    fun `formattedDuration shows less than 1 min for small durations`() {
        val item = createContentItem(durationSeconds = 30)

        assertThat(item.formattedDuration).isEqualTo("< 1 min")
    }

    @Test
    fun `formattedDuration shows less than 1 min for zero duration`() {
        val item = createContentItem(durationSeconds = 0)

        assertThat(item.formattedDuration).isEqualTo("< 1 min")
    }

    @Test
    fun `formattedDuration handles large durations`() {
        val item = createContentItem(durationSeconds = 36000) // 10h

        assertThat(item.formattedDuration).isEqualTo("10h 0m")
    }

    @Test
    fun `formattedDuration calculates correctly for edge case 59 seconds`() {
        val item = createContentItem(durationSeconds = 59)

        assertThat(item.formattedDuration).isEqualTo("< 1 min")
    }

    @Test
    fun `formattedDuration calculates correctly for exactly 1 minute`() {
        val item = createContentItem(durationSeconds = 60)

        assertThat(item.formattedDuration).isEqualTo("1 min")
    }

    @Test
    fun `formattedDuration calculates correctly for exactly 1 hour`() {
        val item = createContentItem(durationSeconds = 3600)

        assertThat(item.formattedDuration).isEqualTo("1h 0m")
    }

    private fun createContentItem(durationSeconds: Long) = ContentItem(
        id = "test",
        name = "Test Item",
        description = "Description",
        imageUrl = "https://example.com/img.jpg",
        durationSeconds = durationSeconds,
        authorOrPodcastName = "Author"
    )
}

class SectionTypeTest {

    @Test
    fun `from maps square correctly`() {
        assertThat(SectionType.from("square")).isEqualTo(SectionType.SQUARE)
    }

    @Test
    fun `from maps big_square correctly`() {
        assertThat(SectionType.from("big_square")).isEqualTo(SectionType.BIG_SQUARE)
    }

    @Test
    fun `from maps big space square correctly`() {
        assertThat(SectionType.from("big square")).isEqualTo(SectionType.BIG_SQUARE)
    }

    @Test
    fun `from maps 2_lines_grid correctly`() {
        assertThat(SectionType.from("2_lines_grid")).isEqualTo(SectionType.TWO_LINES_GRID)
    }

    @Test
    fun `from maps queue correctly`() {
        assertThat(SectionType.from("queue")).isEqualTo(SectionType.QUEUE)
    }

    @Test
    fun `from handles case insensitive input`() {
        assertThat(SectionType.from("SQUARE")).isEqualTo(SectionType.SQUARE)
        assertThat(SectionType.from("Queue")).isEqualTo(SectionType.QUEUE)
    }

    @Test
    fun `from handles whitespace`() {
        assertThat(SectionType.from("  square  ")).isEqualTo(SectionType.SQUARE)
    }

    @Test
    fun `from defaults to SQUARE for unknown type`() {
        assertThat(SectionType.from("unknown")).isEqualTo(SectionType.SQUARE)
        assertThat(SectionType.from("")).isEqualTo(SectionType.SQUARE)
    }
}

class ContentTypeTest {

    @Test
    fun `from maps podcast correctly`() {
        assertThat(ContentType.from("podcast")).isEqualTo(ContentType.PODCAST)
    }

    @Test
    fun `from maps episode correctly`() {
        assertThat(ContentType.from("episode")).isEqualTo(ContentType.EPISODE)
    }

    @Test
    fun `from maps audio_book correctly`() {
        assertThat(ContentType.from("audio_book")).isEqualTo(ContentType.AUDIO_BOOK)
    }

    @Test
    fun `from maps audio_article correctly`() {
        assertThat(ContentType.from("audio_article")).isEqualTo(ContentType.AUDIO_ARTICLE)
    }

    @Test
    fun `from handles case insensitive input`() {
        assertThat(ContentType.from("PODCAST")).isEqualTo(ContentType.PODCAST)
        assertThat(ContentType.from("Episode")).isEqualTo(ContentType.EPISODE)
    }

    @Test
    fun `from defaults to PODCAST for unknown type`() {
        assertThat(ContentType.from("unknown")).isEqualTo(ContentType.PODCAST)
        assertThat(ContentType.from("")).isEqualTo(ContentType.PODCAST)
    }
}

class PaginatedResultTest {

    @Test
    fun `hasMorePages is true when nextPage is not null`() {
        val result = PaginatedResult(
            data = listOf("item"),
            currentPage = 1,
            totalPages = 3,
            nextPage = 2,
            hasMorePages = true
        )

        assertThat(result.hasMorePages).isTrue()
    }

    @Test
    fun `hasMorePages is false when nextPage is null`() {
        val result = PaginatedResult(
            data = listOf("item"),
            currentPage = 3,
            totalPages = 3,
            nextPage = null,
            hasMorePages = false
        )

        assertThat(result.hasMorePages).isFalse()
    }
}
