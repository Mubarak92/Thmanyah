package com.mubarak.thmanyah.data.repository

import com.mubarak.thmanyah.core.common.result.Resource
import com.mubarak.thmanyah.data.datasource.remote.RemoteDataSource
import com.mubarak.thmanyah.data.mapper.SectionMapper
import com.mubarak.thmanyah.domain.model.PaginatedSections
import com.mubarak.thmanyah.domain.model.Section
import com.mubarak.thmanyah.domain.repository.HomeRepository
import com.mubarak.thmanyah.domain.repository.SearchRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: SectionMapper
) : HomeRepository {
    override suspend fun getHomeSections(page: Int): Resource<PaginatedSections> = try {
        val response = remoteDataSource.getHomeSections(page)
        Resource.success(mapper.mapHomeSections(response, page))
    } catch (e: Exception) {
        Resource.error(e)
    }
}

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val mapper: SectionMapper
) : SearchRepository {
    override suspend fun search(query: String): Resource<List<Section>> = try {
        val response = remoteDataSource.search(query)
        Resource.success(mapper.mapSearchResults(response))
    } catch (e: Exception) {
        Resource.error(e)
    }
}
