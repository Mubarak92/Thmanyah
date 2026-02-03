package com.mubarak.thmanyah.data.datasource.remote

import com.mubarak.thmanyah.data.datasource.remote.api.ThmanyahApi
import com.mubarak.thmanyah.data.datasource.remote.dto.response.HomeSectionsResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SearchResponseDto
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getHomeSections(page: Int): HomeSectionsResponseDto
    suspend fun search(query: String): SearchResponseDto
}

class RemoteDataSourceImpl @Inject constructor(
    private val api: ThmanyahApi
) : RemoteDataSource {
    override suspend fun getHomeSections(page: Int) = api.getHomeSections(page)
    override suspend fun search(query: String) = api.search(query = query)
}
