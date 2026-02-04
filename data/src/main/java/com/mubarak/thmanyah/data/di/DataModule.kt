package com.mubarak.thmanyah.data.di

import com.mubarak.thmanyah.core.network.handler.NetworkHandler
import com.mubarak.thmanyah.data.datasource.remote.RemoteDataSource
import com.mubarak.thmanyah.data.datasource.remote.RemoteDataSourceImpl
import com.mubarak.thmanyah.data.datasource.remote.api.ThmanyahApi
import com.mubarak.thmanyah.data.mapper.SectionMapper
import com.mubarak.thmanyah.data.repository.HomeRepositoryImpl
import com.mubarak.thmanyah.data.repository.SearchRepositoryImpl
import com.mubarak.thmanyah.domain.repository.HomeRepository
import com.mubarak.thmanyah.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideThmanyahApi(networkHandler: NetworkHandler): ThmanyahApi =
        networkHandler.create(ThmanyahApi::class.java)

    @Provides
    @Singleton
    fun provideSectionMapper(): SectionMapper = SectionMapper()

    @Provides
    @Singleton
    fun provideRemoteDataSource(api: ThmanyahApi): RemoteDataSource =
        RemoteDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideHomeRepository(
        remoteDataSource: RemoteDataSource,
        mapper: SectionMapper
    ): HomeRepository = HomeRepositoryImpl(remoteDataSource, mapper)

    @Provides
    @Singleton
    fun provideSearchRepository(
        remoteDataSource: RemoteDataSource,
        mapper: SectionMapper
    ): SearchRepository = SearchRepositoryImpl(remoteDataSource, mapper)
}
