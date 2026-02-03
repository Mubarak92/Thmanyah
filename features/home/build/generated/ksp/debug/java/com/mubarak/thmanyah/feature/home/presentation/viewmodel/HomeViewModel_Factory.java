package com.mubarak.thmanyah.feature.home.presentation.viewmodel;

import com.mubarak.thmanyah.domain.usecase.GetHomeSectionsUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetHomeSectionsUseCase> getHomeSectionsUseCaseProvider;

  private HomeViewModel_Factory(Provider<GetHomeSectionsUseCase> getHomeSectionsUseCaseProvider) {
    this.getHomeSectionsUseCaseProvider = getHomeSectionsUseCaseProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getHomeSectionsUseCaseProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetHomeSectionsUseCase> getHomeSectionsUseCaseProvider) {
    return new HomeViewModel_Factory(getHomeSectionsUseCaseProvider);
  }

  public static HomeViewModel newInstance(GetHomeSectionsUseCase getHomeSectionsUseCase) {
    return new HomeViewModel(getHomeSectionsUseCase);
  }
}
