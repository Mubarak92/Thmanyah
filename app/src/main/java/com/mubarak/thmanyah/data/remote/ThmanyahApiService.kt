package com.mubarak.thmanyah.data.remote

import com.mubarak.thmanyah.data.model.HomeSectionsResponse
import com.mubarak.thmanyah.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ThmanyahApiService {

    @GET("home_sections")
    suspend fun getHomeSections(
        @Query("page") page: Int = 1
    ): HomeSectionsResponse

    @GET
    suspend fun search(
        @Url url: String = "https://mock.apidog.com/m1/735111-711675-default/search",
        @Query("q") query: String
    ): SearchResponse
}