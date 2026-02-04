package com.mubarak.thmanyah.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.core.testing.fake.FakeHomeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetHomeSectionsUseCaseTest {

    private lateinit var useCase: GetHomeSectionsUseCase
    private lateinit var fakeRepository: FakeHomeRepository

    @Before
    fun setup() {
        fakeRepository = FakeHomeRepository()
        useCase = GetHomeSectionsUseCase(fakeRepository)
    }

    @Test
    fun `invoke with valid page returns success`() = runTest {
        // When
        val result = useCase(page = 1)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.currentPage).isEqualTo(1)
        assertThat(data.data).isNotEmpty()
    }

    @Test
    fun `invoke returns sections with correct pagination info`() = runTest {
        // Given
        fakeRepository.totalPages = 5

        // When
        val result = useCase(page = 2)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.currentPage).isEqualTo(2)
        assertThat(data.totalPages).isEqualTo(5)
        assertThat(data.hasMorePages).isTrue()
        assertThat(data.nextPage).isEqualTo(3)
    }

    @Test
    fun `invoke on last page returns hasMorePages false`() = runTest {
        // Given
        fakeRepository.totalPages = 3

        // When
        val result = useCase(page = 3)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.hasMorePages).isFalse()
        assertThat(data.nextPage).isNull()
    }

    @Test
    fun `invoke with repository error returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Network error"

        // When
        val result = useCase(page = 1)

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Network error")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invoke with page 0 throws exception`() = runTest {
        useCase(page = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invoke with negative page throws exception`() = runTest {
        useCase(page = -1)
    }

    @Test
    fun `invoke calls repository with correct page`() = runTest {
        // When
        useCase(page = 5)

        // Then
        assertThat(fakeRepository.getLoadedPages()).contains(5)
    }

    @Test
    fun `default page is 1`() = runTest {
        // When
        val result = useCase()

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val data = (result as Resource.Success).data
        assertThat(data.currentPage).isEqualTo(1)
    }

    @Test
    fun `sections contain expected content items`() = runTest {
        // When
        val result = useCase(page = 1)

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val sections = (result as Resource.Success).data.data
        assertThat(sections).isNotEmpty()
        sections.forEach { section ->
            assertThat(section.items).isNotEmpty()
            assertThat(section.id).isNotEmpty()
            assertThat(section.name).isNotEmpty()
        }
    }
}
