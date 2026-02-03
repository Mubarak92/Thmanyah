package com.mubarak.thmanyah.data.datasource.remote;

import com.mubarak.thmanyah.data.datasource.remote.api.ThmanyahApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class RemoteDataSourceImpl_Factory implements Factory<RemoteDataSourceImpl> {
  private final Provider<ThmanyahApi> apiProvider;

  private RemoteDataSourceImpl_Factory(Provider<ThmanyahApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public RemoteDataSourceImpl get() {
    return newInstance(apiProvider.get());
  }

  public static RemoteDataSourceImpl_Factory create(Provider<ThmanyahApi> apiProvider) {
    return new RemoteDataSourceImpl_Factory(apiProvider);
  }

  public static RemoteDataSourceImpl newInstance(ThmanyahApi api) {
    return new RemoteDataSourceImpl(api);
  }
}
