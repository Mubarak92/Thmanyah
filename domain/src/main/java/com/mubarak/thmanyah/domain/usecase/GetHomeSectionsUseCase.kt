package com.mubarak.thmanyah.domain.usecase

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.domain.model.PaginatedSections
import com.mubarak.thmanyah.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeSectionsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(page: Int = 1): Resource<PaginatedSections> {
        require(page > 0) { "Page must be > 0" }
        return repository.getHomeSections(page)
    }
}
