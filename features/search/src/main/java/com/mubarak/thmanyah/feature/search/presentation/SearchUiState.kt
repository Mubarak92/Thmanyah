package com.mubarak.thmanyah.feature.search.presentation

import com.mubarak.thmanyah.domain.model.Section

data class SearchUiState(
    val query: String = "",
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
) {
    val showEmptyState get() = hasSearched && sections.isEmpty() && !isLoading && error == null
    val showResults get() = sections.isNotEmpty()
    val showInitialState get() = !hasSearched && query.isBlank()
}
