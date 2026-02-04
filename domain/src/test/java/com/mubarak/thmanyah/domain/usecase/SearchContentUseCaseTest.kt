package com.mubarak.thmanyah.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.core.testing.fake.FakeSearchRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchContentUseCaseTest {

    private lateinit var useCase: SearchContentUseCase
    private lateinit var fakeRepository: FakeSearchRepository

    @Before
    fun setup() {
        fakeRepository = FakeSearchRepository()
        useCase = SearchContentUseCase(fakeRepository)
    }

    @Test
    fun `invoke with valid query returns success`() = runTest {
        // When
        val result = useCase("tech")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isNotEmpty()
    }

    @Test
    fun `invoke with blank query returns empty list without calling repository`() = runTest {
        // When
        val result = useCase("")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEmpty()
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(0)
    }

    @Test
    fun `invoke with whitespace only query returns empty list`() = runTest {
        // When
        val result = useCase("   ")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEmpty()
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(0)
    }

    @Test
    fun `invoke trims query before searching`() = runTest {
        // When
        useCase("  tech  ")

        // Then
        assertThat(fakeRepository.searchQueries).contains("tech")
    }

    @Test
    fun `invoke with repository error returns error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Search failed"

        // When
        val result = useCase("test")

        // Then
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo("Search failed")
    }

    @Test
    fun `invoke calls repository exactly once per valid query`() = runTest {
        // When
        useCase("query1")
        useCase("query2")
        useCase("") // Should not call repository

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(2)
    }

    @Test
    fun `search results contain sections with items`() = runTest {
        // When
        val result = useCase("podcast")

        // Then
        assertThat(result).isInstanceOf(Resource.Success::class.java)
        val sections = (result as Resource.Success).data
        assertThat(sections).isNotEmpty()
        sections.forEach { section ->
            assertThat(section.items).isNotEmpty()
        }
    }

    @Test
    fun `multiple searches track all queries`() = runTest {
        // When
        useCase("first")
        useCase("second")
        useCase("third")

        // Then
        assertThat(fakeRepository.searchQueries).containsExactly("first", "second", "third")
    }
}
