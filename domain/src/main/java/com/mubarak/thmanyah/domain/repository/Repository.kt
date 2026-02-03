package com.mubarak.thmanyah.domain.repository

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.model.PaginatedSections
import com.mubarak.thmanyah.domain.model.Section

interface HomeRepository {
    suspend fun getHomeSections(page: Int): Resource<PaginatedSections>
}

interface SearchRepository {
    suspend fun search(query: String): Resource<List<Section>>
}
