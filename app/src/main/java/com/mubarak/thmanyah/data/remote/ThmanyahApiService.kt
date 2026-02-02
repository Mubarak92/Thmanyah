package com.mubarak.thmanyah.data.remote

import com.mubarak.thmanyah.data.model.HomeSectionsResponse
import com.mubarak.thmanyah.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ThmanyahApiService {

    @GET(HOME_BASE_URL)
    suspend fun getHomeSections(
        @Query("page") page: Int = 1
    ): HomeSectionsResponse

    @GET(SEARCH_BASE_URL)
    suspend fun search(
        @Query("q") query: String
    ): SearchResponse

    companion object {
        const val HOME_BASE_URL = "https://api-v2-b2sit6oh3a-uc.a.run.app/home_sections"
        const val SEARCH_BASE_URL = "https://mock.apidog.com/m1/735111-711675-default/search"
    }
}