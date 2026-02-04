package com.mubarak.thmanyah.feature.search.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.mubarak.thmanyah.core.common.base.BaseViewModel
import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.usecase.SearchContentUseCase
import com.mubarak.thmanyah.feature.search.presentation.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchContentUseCase: SearchContentUseCase
) : BaseViewModel<SearchUiState>(SearchUiState()) {

    private val queryFlow = MutableStateFlow("")

    init { observeQueryChanges() }

    fun onQueryChanged(query: String) {
        updateState { copy(query = query) }
        queryFlow.value = query
        if (query.isBlank()) updateState { copy(sections = emptyList(), error = null, hasSearched = false) }
    }

    fun clearQuery() = onQueryChanged("")

    fun retry() {
        if (currentState.query.isNotBlank()) {
            viewModelScope.launch { executeSearch(currentState.query) }
        }
    }

    private fun observeQueryChanges() {
        viewModelScope.launch {
            queryFlow.debounce(300).distinctUntilChanged().filter { it.isNotBlank() }
                .collectLatest { query -> executeSearch(query) }
        }
    }

    private suspend fun executeSearch(query: String) {
        updateState { copy(isLoading = true, error = null) }
        when (val result = searchContentUseCase(query)) {
            is Resource.Success -> updateState { copy(sections = result.data, isLoading = false, hasSearched = true) }
            is Resource.Error -> updateState { copy(isLoading = false, error = result.message) }
            is Resource.Loading -> {}
            is Resource.Empty -> updateState { copy(sections = emptyList(), isLoading = false, hasSearched = true) }
        }
    }
}
