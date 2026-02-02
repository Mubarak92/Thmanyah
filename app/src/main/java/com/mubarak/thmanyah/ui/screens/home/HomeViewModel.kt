package com.mubarak.thmanyah.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mubarak.thmanyah.data.model.Section
import com.mubarak.thmanyah.data.repository.ThmanyahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ThmanyahRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSections(page = 1)
    }

    fun refresh() {
        _uiState.update { HomeUiState() }
        loadSections(page = 1)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || !state.hasMorePages) return

        val nextPage = state.currentPage + 1
        loadSections(page = nextPage, isLoadMore = true)
    }

    private fun loadSections(page: Int, isLoadMore: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                if (isLoadMore) it.copy(isLoadingMore = true)
                else it.copy(isLoading = true, error = null)
            }

            repository.getHomeSections(page).fold(
                onSuccess = { result ->
                    _uiState.update { current ->
                        current.copy(
                            sections = if (isLoadMore) current.sections + result.sections
                            else result.sections,
                            isLoading = false,
                            isLoadingMore = false,
                            currentPage = page,
                            hasMorePages = result.nextPage != null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = throwable.localizedMessage ?: "Unknown error"
                        )
                    }
                }
            )
        }
    }
}