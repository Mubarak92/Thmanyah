package com.mubarak.thmanyah.data.repository

import com.mubarak.thmanyah.data.model.Section
import com.mubarak.thmanyah.data.model.SectionMapper
import com.mubarak.thmanyah.data.remote.ThmanyahApiService
import javax.inject.Inject

interface ThmanyahRepository {
    suspend fun getHomeSections(page: Int): Result<HomeSectionsResult>
    suspend fun search(query: String): Result<List<Section>>
}

data class HomeSectionsResult(
    val sections: List<Section>,
    val totalPages: Int,
    val nextPage: Int?
)

class ThmanyahRepositoryImpl @Inject constructor(
    private val api: ThmanyahApiService
) : ThmanyahRepository {

    override suspend fun getHomeSections(page: Int): Result<HomeSectionsResult> {
        return try {
            val response = api.getHomeSections(page = page)
            val sections = SectionMapper.mapSections(response.sections)
            val totalPages = response.pagination?.totalPages ?: 1
            val nextPage = response.pagination?.nextPage?.let { extractPage(it) }
            Result.success(HomeSectionsResult(sections, totalPages, nextPage))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun search(query: String): Result<List<Section>> {
        return try {
            val response = api.search(query = query)
            val sections = SectionMapper.mapSections(response.sections)
            Result.success(sections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractPage(path: String): Int? {
        return Regex("page=(\\d+)").find(path)?.groupValues?.get(1)?.toIntOrNull()
    }
}