package com.mubarak.thmanyah.data.datasource.remote.api

import com.mubarak.thmanyah.data.datasource.remote.dto.response.HomeSectionsResponseDto
import com.mubarak.thmanyah.data.datasource.remote.dto.response.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ThmanyahApi {

    @GET("home_sections")
    suspend fun getHomeSections(@Query("page") page: Int = 1): HomeSectionsResponseDto

    @GET
    suspend fun search(
        @Url url: String = SEARCH_URL,
        @Query("q") query: String
    ): SearchResponseDto

    companion object {
        const val SEARCH_URL = "https://mock.apidog.com/m1/735111-711675-default/search"
    }
}
