package com.mubarak.thmanyah.core.network.di;

import com.mubarak.thmanyah.core.network.handler.NetworkConfig;
import com.mubarak.thmanyah.core.network.handler.NetworkHandler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideNetworkHandlerFactory implements Factory<NetworkHandler> {
  private final Provider<NetworkConfig> configProvider;

  private NetworkModule_ProvideNetworkHandlerFactory(Provider<NetworkConfig> configProvider) {
    this.configProvider = configProvider;
  }

  @Override
  public NetworkHandler get() {
    return provideNetworkHandler(configProvider.get());
  }

  public static NetworkModule_ProvideNetworkHandlerFactory create(
      Provider<NetworkConfig> configProvider) {
    return new NetworkModule_ProvideNetworkHandlerFactory(configProvider);
  }

  public static NetworkHandler provideNetworkHandler(NetworkConfig config) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideNetworkHandler(config));
  }
}
