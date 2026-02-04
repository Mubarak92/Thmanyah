package com.mubarak.thmanyah.feature.search.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mubarak.thmanyah.core.testing.fake.FakeSearchRepository
import com.mubarak.thmanyah.core.testing.rule.MainDispatcherRule
import com.mubarak.thmanyah.domain.usecase.SearchContentUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.text.contains

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var fakeRepository: FakeSearchRepository
    private lateinit var useCase: SearchContentUseCase

    @Before
    fun setup() {
        fakeRepository = FakeSearchRepository()
        useCase = SearchContentUseCase(fakeRepository)
        viewModel = SearchViewModel(useCase)
    }


    @Test
    fun `initial state has empty query and no results`() {
        val state = viewModel.uiState.value

        assertThat(state.query).isEmpty()
        assertThat(state.sections).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.hasSearched).isFalse()
    }

    @Test
    fun `initial state shows initial state UI`() {
        val state = viewModel.uiState.value

        assertThat(state.showInitialState).isTrue()
        assertThat(state.showResults).isFalse()
        assertThat(state.showEmptyState).isFalse()
    }


    @Test
    fun `onQueryChanged updates query in state`() = runTest {
        // When
        viewModel.onQueryChanged("test")

        // Then
        assertThat(viewModel.uiState.value.query).isEqualTo("test")
    }

    @Test
    fun `onQueryChanged triggers search after debounce`() = runTest {
        // When
        viewModel.onQueryChanged("podcast")
        advanceTimeBy(350) // Debounce is 300ms
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(1)
        assertThat(fakeRepository.searchQueries).contains("podcast")
    }

    @Test
    fun `rapid typing only triggers one search after debounce`() = runTest {
        // When - rapid typing
        viewModel.onQueryChanged("p")
        advanceTimeBy(100)
        viewModel.onQueryChanged("po")
        advanceTimeBy(100)
        viewModel.onQueryChanged("pod")
        advanceTimeBy(100)
        viewModel.onQueryChanged("podc")
        advanceTimeBy(100)
        viewModel.onQueryChanged("podca")
        advanceTimeBy(100)
        viewModel.onQueryChanged("podcas")
        advanceTimeBy(100)
        viewModel.onQueryChanged("podcast")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then - only final query should be searched
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(1)
        assertThat(fakeRepository.searchQueries).containsExactly("podcast")
    }

    @Test
    fun `blank query clears results without searching`() = runTest {
        // Given - perform a search first
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.sections).isNotEmpty()
        val searchCountAfterFirstSearch = fakeRepository.getSearchCallCount()

        // When - clear query
        viewModel.onQueryChanged("")

        // Then
        val state = viewModel.uiState.value
        assertThat(state.query).isEmpty()
        assertThat(state.sections).isEmpty()
        assertThat(state.hasSearched).isFalse()
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(searchCountAfterFirstSearch)
    }

    @Test
    fun `whitespace only query is treated as blank`() = runTest {
        // When
        viewModel.onQueryChanged("   ")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(0)
        assertThat(viewModel.uiState.value.sections).isEmpty()
    }


    @Test
    fun `successful search updates sections`() = runTest {
        // When
        viewModel.onQueryChanged("tech")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.sections).isNotEmpty()
        assertThat(state.hasSearched).isTrue()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `search shows loading state`() = runTest {
        fakeRepository.delayMs = 100

        viewModel.uiState.test {
            skipItems(1) // Skip initial state

            viewModel.onQueryChanged("test")
            advanceTimeBy(350) // Past debounce

            // Should show loading
            val loadingState = awaitItem() // query update
            awaitItem() // loading state
            assertThat(viewModel.uiState.value.isLoading || loadingState.isLoading).isTrue()

            advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search error updates error state`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Search failed"

        // When
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.error).isEqualTo("Search failed")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `empty search results shows empty state`() = runTest {
        // Given
        fakeRepository.searchResults = emptyList()

        // When
        viewModel.onQueryChanged("xyz123")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.showEmptyState).isTrue()
        assertThat(state.hasSearched).isTrue()
        assertThat(state.sections).isEmpty()
    }


    @Test
    fun `clearQuery resets to initial state`() = runTest {
        // Given - perform search first
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.sections).isNotEmpty()

        // When
        viewModel.clearQuery()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.query).isEmpty()
        assertThat(state.sections).isEmpty()
        assertThat(state.hasSearched).isFalse()
        assertThat(state.showInitialState).isTrue()
    }


    @Test
    fun `retry performs search with current query`() = runTest {
        // Given - failed search
        fakeRepository.shouldReturnError = true
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.error).isNotNull()

        val searchCountBeforeRetry = fakeRepository.getSearchCallCount()

        // When
        fakeRepository.shouldReturnError = false
        viewModel.retry()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.error).isNull()
        assertThat(state.sections).isNotEmpty()
        assertThat(fakeRepository.getSearchCallCount()).isGreaterThan(searchCountBeforeRetry)
    }

    @Test
    fun `retry does nothing with blank query`() = runTest {
        // Given
        viewModel.onQueryChanged("")
        val searchCountBefore = fakeRepository.getSearchCallCount()

        // When
        viewModel.retry()
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(searchCountBefore)
    }


    @Test
    fun `showResults is true when sections exist`() = runTest {
        // When
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.showResults).isTrue()
    }

    @Test
    fun `showEmptyState is true after search with no results`() = runTest {
        // Given
        fakeRepository.searchResults = emptyList()

        // When
        viewModel.onQueryChanged("nonexistent")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.showEmptyState).isTrue()
        assertThat(state.showResults).isFalse()
        assertThat(state.showInitialState).isFalse()
    }

    @Test
    fun `showInitialState is true only before any search`() = runTest {
        // Initially true
        assertThat(viewModel.uiState.value.showInitialState).isTrue()

        // After search - false
        viewModel.onQueryChanged("test")
        advanceTimeBy(350)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.showInitialState).isFalse()

        // After clear - true again
        viewModel.clearQuery()
        assertThat(viewModel.uiState.value.showInitialState).isTrue()
    }


    @Test
    fun `search is not triggered before debounce time`() = runTest {
        // When
        viewModel.onQueryChanged("test")
        advanceTimeBy(200) // Less than 300ms debounce

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(0)
    }

    @Test
    fun `search is triggered after debounce time`() = runTest {
        // When
        viewModel.onQueryChanged("test")
        advanceTimeBy(350) // More than 300ms debounce
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(1)
    }

    @Test
    fun `new query resets debounce timer`() = runTest {
        // When
        viewModel.onQueryChanged("test1")
        advanceTimeBy(200) // 200ms
        viewModel.onQueryChanged("test2") // Reset timer
        advanceTimeBy(200) // 200ms more (400ms total, but only 200ms since last change)

        // Then - should not have searched yet
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(0)

        // When - wait full debounce from last change
        advanceTimeBy(150)
        advanceUntilIdle()

        // Then - should have searched for "test2"
        assertThat(fakeRepository.getSearchCallCount()).isEqualTo(1)
        assertThat(fakeRepository.searchQueries).containsExactly("test2")
    }


    @Test
    fun `new search cancels previous pending search`() = runTest {
        // Given
        fakeRepository.delayMs = 500

        // When - start first search
        viewModel.onQueryChanged("first")
        advanceTimeBy(350) // Trigger first search
        advanceTimeBy(100) // Let it start but not complete

        // Then - start second search
        viewModel.onQueryChanged("second")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then - only second search result should be shown
        assertThat(viewModel.uiState.value.query).isEqualTo("second")
    }
}
