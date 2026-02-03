package com.mubarak.thmanyah.feature.home.presentation

import com.mubarak.thmanyah.domain.model.Section
import kotlin.collections.isNotEmpty

data class HomeUiState(
    val sections: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true
) {
    val isEmpty get() = sections.isEmpty() && !isLoading
    val showContent get() = sections.isNotEmpty()
    val showFullScreenError get() = error != null && sections.isEmpty()
}
