package com.mubarak.thmanyah.domain.usecase

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.model.Section
import com.mubarak.thmanyah.domain.repository.SearchRepository
import javax.inject.Inject

class SearchContentUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): Resource<List<Section>> {
        if (query.isBlank()) return Resource.success(emptyList())
        return repository.search(query.trim())
    }
}
