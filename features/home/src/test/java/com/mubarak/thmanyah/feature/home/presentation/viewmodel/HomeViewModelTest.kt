package com.mubarak.thmanyah.feature.home.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mubarak.thmanyah.core.testing.fake.FakeHomeRepository
import com.mubarak.thmanyah.core.testing.rule.MainDispatcherRule
import com.mubarak.thmanyah.domain.usecase.GetHomeSectionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeRepository: FakeHomeRepository
    private lateinit var useCase: GetHomeSectionsUseCase

    @Before
    fun setup() {
        fakeRepository = FakeHomeRepository()
        useCase = GetHomeSectionsUseCase(fakeRepository)
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(useCase)
    }


    @Test
    fun `initial state loads first page`() = runTest {
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.sections).isNotEmpty()
        assertThat(state.currentPage).isEqualTo(1)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `initial state shows loading then content`() = runTest {
        viewModel = createViewModel()

        viewModel.uiState.test {
            // Initial state
            val initial = awaitItem()
            assertThat(initial.isLoading).isTrue()

            // After load
            val loaded = awaitItem()
            assertThat(loaded.isLoading).isFalse()
            assertThat(loaded.sections).isNotEmpty()
        }
    }

    @Test
    fun `initial load error shows error state`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        fakeRepository.errorMessage = "Network error"

        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.error).isEqualTo("Network error")
        assertThat(state.sections).isEmpty()
        assertThat(state.isLoading).isFalse()
    }


    @Test
    fun `refresh reloads first page`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()
        val initialCallCount = fakeRepository.getLoadCallCount()

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getLoadCallCount()).isGreaterThan(initialCallCount)
        assertThat(fakeRepository.getLoadedPages().last()).isEqualTo(1)
    }

    @Test
    fun `refresh clears error and shows refreshing state`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.error).isNotNull()

        // When
        fakeRepository.shouldReturnError = false
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.error).isNull()
        assertThat(state.sections).isNotEmpty()
    }

    @Test
    fun `refresh replaces existing sections`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()
        val oldSections = viewModel.uiState.value.sections

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then
        val newSections = viewModel.uiState.value.sections
        assertThat(newSections).isNotEmpty()
        assertThat(newSections.map { it.id }).isNotEqualTo(oldSections.map { it.id })
    }


    @Test
    fun `loadMore loads next page`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(1)

        // When
        viewModel.loadMore()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(2)
        assertThat(fakeRepository.getLoadedPages()).contains(2)
    }

    @Test
    fun `loadMore appends sections to existing list`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()
        val initialCount = viewModel.uiState.value.sections.size

        // When
        viewModel.loadMore()
        advanceUntilIdle()

        // Then
        val newCount = viewModel.uiState.value.sections.size
        assertThat(newCount).isGreaterThan(initialCount)
    }

    @Test
    fun `loadMore does nothing when already loading`() = runTest {
        // Given
        fakeRepository.delayMs = 1000
        viewModel = createViewModel()
        // Don't wait - still loading initial

        // When
        viewModel.loadMore()
        viewModel.loadMore()
        viewModel.loadMore()
        advanceUntilIdle()

        // Then - should only have called for page 1 (initial load), not additional pages
        assertThat(fakeRepository.getLoadedPages().count { it == 1 }).isEqualTo(1)
    }

    @Test
    fun `loadMore does nothing when no more pages`() = runTest {
        // Given
        fakeRepository.totalPages = 1
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.loadMore()
        advanceUntilIdle()

        // Then
        assertThat(fakeRepository.getLoadCallCount()).isEqualTo(1) // Only initial load
        assertThat(viewModel.uiState.value.hasMorePages).isFalse()
    }

    @Test
    fun `loadMore shows isLoadingMore state`() = runTest {
        // Given
        fakeRepository.delayMs = 100
        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            skipItems(1) // Skip current state

            // When
            viewModel.loadMore()

            // Then
            val loadingState = awaitItem()
            assertThat(loadingState.isLoadingMore).isTrue()

            val loadedState = awaitItem()
            assertThat(loadedState.isLoadingMore).isFalse()
        }
    }

    @Test
    fun `can load all pages sequentially`() = runTest {
        // Given
        fakeRepository.totalPages = 3
        viewModel = createViewModel()
        advanceUntilIdle()

        // When - load all pages
        viewModel.loadMore() // Page 2
        advanceUntilIdle()
        viewModel.loadMore() // Page 3
        advanceUntilIdle()
        viewModel.loadMore() // Should not load page 4
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(3)
        assertThat(viewModel.uiState.value.hasMorePages).isFalse()
        assertThat(fakeRepository.getLoadedPages()).containsExactly(1, 2, 3)
    }

    // ==================== Retry Tests ====================

    @Test
    fun `retry reloads current page after error`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.error).isNotNull()

        // When
        fakeRepository.shouldReturnError = false
        viewModel.retry()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.error).isNull()
        assertThat(state.sections).isNotEmpty()
    }

    @Test
    fun `retry after loadMore error retries that page`() = runTest {
        // Given - successful first load
        viewModel = createViewModel()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.currentPage).isEqualTo(1)

        // When - loadMore fails
        fakeRepository.shouldReturnError = true
        viewModel.loadMore()
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.error).isNotNull()

        // Then - retry succeeds
        fakeRepository.shouldReturnError = false
        viewModel.retry()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.error).isNull()
    }

    // ==================== UI State Properties Tests ====================

    @Test
    fun `isEmpty returns true when no sections and not loading`() = runTest {
        // Given
        fakeRepository.sectionsPerPage = 0
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.isEmpty).isTrue()
    }

    @Test
    fun `showContent returns true when sections exist`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.uiState.value.showContent).isTrue()
    }

    @Test
    fun `showFullScreenError returns true when error and no sections`() = runTest {
        // Given
        fakeRepository.shouldReturnError = true
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.showFullScreenError).isTrue()
        assertThat(state.error).isNotNull()
        assertThat(state.sections).isEmpty()
    }

    @Test
    fun `error during loadMore does not show full screen error`() = runTest {
        // Given - successful first load
        viewModel = createViewModel()
        advanceUntilIdle()

        // When - loadMore fails
        fakeRepository.shouldReturnError = true
        viewModel.loadMore()
        advanceUntilIdle()

        // Then - has error but not full screen (sections exist)
        val state = viewModel.uiState.value
        assertThat(state.error).isNotNull()
        assertThat(state.sections).isNotEmpty()
        assertThat(state.showFullScreenError).isFalse()
    }
}
