package com.mubarak.thmanyah.feature.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mubarak.thmanyah.core.common.base.BaseViewModel
import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.usecase.GetHomeSectionsUseCase
import com.mubarak.thmanyah.feature.home.presentation.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSectionsUseCase: GetHomeSectionsUseCase
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    init { loadSections(page = 1) }

    fun refresh() { loadSections(page = 1, isRefresh = true) }

    fun loadMore() {
        if (currentState.isLoading || currentState.isLoadingMore || !currentState.hasMorePages) return
        loadSections(page = currentState.currentPage + 1, isLoadMore = true)
    }

    fun retry() { loadSections(page = if (currentState.sections.isEmpty()) 1 else currentState.currentPage) }

    private fun loadSections(page: Int, isLoadMore: Boolean = false, isRefresh: Boolean = false) {
        if (currentState.isLoading || currentState.isLoadingMore) return

        viewModelScope.launch {
            updateState {
                when {
                    isLoadMore -> copy(isLoadingMore = true)
                    isRefresh -> copy(isRefreshing = true, error = null)
                    else -> copy(isLoading = true, error = null)
                }
            }

            when (val result = getHomeSectionsUseCase(page)) {
                is Resource.Success -> updateState {
                    copy(
                        sections = if (isLoadMore) sections + result.data.data else result.data.data,
                        isLoading = false, isLoadingMore = false, isRefreshing = false,
                        currentPage = result.data.currentPage,
                        hasMorePages = result.data.hasMorePages, error = null
                    )
                }
                is Resource.Error -> updateState {
                    copy(isLoading = false, isLoadingMore = false, isRefreshing = false, error = result.message)
                }
                is Resource.Loading -> {}
                is Resource.Empty -> updateState {
                    copy(sections = emptyList(), isLoading = false, isLoadingMore = false, isRefreshing = false)
                }
            }
        }
    }
}
