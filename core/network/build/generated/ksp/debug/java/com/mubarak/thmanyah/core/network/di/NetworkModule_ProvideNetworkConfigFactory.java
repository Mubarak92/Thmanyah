package com.mubarak.thmanyah.core.network.di;

import com.mubarak.thmanyah.core.network.handler.NetworkConfig;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class NetworkModule_ProvideNetworkConfigFactory implements Factory<NetworkConfig> {
  @Override
  public NetworkConfig get() {
    return provideNetworkConfig();
  }

  public static NetworkModule_ProvideNetworkConfigFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkConfig provideNetworkConfig() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideNetworkConfig());
  }

  private static final class InstanceHolder {
    static final NetworkModule_ProvideNetworkConfigFactory INSTANCE = new NetworkModule_ProvideNetworkConfigFactory();
  }
}
