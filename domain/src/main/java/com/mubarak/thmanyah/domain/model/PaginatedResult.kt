package com.mubarak.thmanyah.domain.model

data class PaginatedResult<T>(
    val data: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val nextPage: Int?,
    val hasMorePages: Boolean
)

typealias PaginatedSections = PaginatedResult<Section>
