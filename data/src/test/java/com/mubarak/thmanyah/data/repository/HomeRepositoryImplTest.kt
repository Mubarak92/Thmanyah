package com.mubarak.thmanyah.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonParser
import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.data.datasource.remote.RemoteDataSource
import com.mubarak.thmanyah.data.datasource.remote.dto.response.HomeSectionsResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.PaginationDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SectionDto
import com.mubarak.thmanyah.data.mapper.SectionMapper
import com.mubarak.thmanyah.data.repository.HomeRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class HomeRepositoryImplTest {

    private lateinit var repository: HomeRepositoryImpl
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var mapper: SectionMapper

    @Before
    fun setup() {
        remoteDataSource = mockk()
        mapper = SectionMapper()
        repository = HomeRepositoryImpl(remoteDataSource, mapper)
    }

    @Test
    fun `getHomeSections returns success when datasource succeeds`() = runTest {
        // Given
        val responseDto = createMockResponse()
        coEvery { remoteDataSource.getHomeSections(1) } returns responseDto

        // When
        val result = repository.getHomeSections(1)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.currentPage).isEqualTo(1)
        assertThat(data.data).isNotEmpty()
    }

    @Test
    fun `getHomeSections returns error when datasource throws exception`() = runTest {
        // Given
        coEvery { remoteDataSource.getHomeSections(any()) } throws IOException("Network error")

        // When
        val result = repository.getHomeSections(1)

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).contains("Network error")
    }

    @Test
    fun `getHomeSections calls datasource with correct page`() = runTest {
        // Given
        val responseDto = createMockResponse()
        coEvery { remoteDataSource.getHomeSections(any()) } returns responseDto

        // When
        repository.getHomeSections(5)

        // Then
        coVerify { remoteDataSource.getHomeSections(5) }
    }

    @Test
    fun `getHomeSections maps pagination correctly`() = runTest {
        // Given
        val responseDto = HomeSectionsResponseDto(
            sections = listOf(createMockSectionDto()),
            pagination = PaginationDto(nextPage = "/home_sections?page=3", totalPages = 10)
        )
        coEvery { remoteDataSource.getHomeSections(2) } returns responseDto

        // When
        val result = repository.getHomeSections(2)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.currentPage).isEqualTo(2)
        assertThat(data.totalPages).isEqualTo(10)
        assertThat(data.nextPage).isEqualTo(3)
        assertThat(data.hasMorePages).isTrue()
    }

    @Test
    fun `getHomeSections handles empty sections`() = runTest {
        // Given
        val responseDto = HomeSectionsResponseDto(
            sections = emptyList(),
            pagination = null
        )
        coEvery { remoteDataSource.getHomeSections(1) } returns responseDto

        // When
        val result = repository.getHomeSections(1)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.data).isEmpty()
    }

    @Test
    fun `getHomeSections handles null pagination`() = runTest {
        // Given
        val responseDto = HomeSectionsResponseDto(
            sections = listOf(createMockSectionDto()),
            pagination = null
        )
        coEvery { remoteDataSource.getHomeSections(1) } returns responseDto

        // When
        val result = repository.getHomeSections(1)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.totalPages).isEqualTo(1)
        assertThat(data.hasMorePages).isFalse()
    }

    private fun createMockResponse() = HomeSectionsResponseDto(
        sections = listOf(createMockSectionDto()),
        pagination = PaginationDto(nextPage = "/home_sections?page=2", totalPages = 5)
    )

    private fun createMockSectionDto() = SectionDto(
        name = "Test Section",
        type = "square",
        contentType = "podcast",
        order = 1,
        content = listOf(
            JsonParser.parseString("""
                {
                    "podcast_id": "123",
                    "name": "Test Podcast",
                    "description": "Description",
                    "avatar_url": "https://example.com/img.jpg",
                    "duration": 3600,
                    "episode_count": 100
                }
            """.trimIndent())
        )
    )
}
