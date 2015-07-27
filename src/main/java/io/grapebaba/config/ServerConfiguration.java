package io.grapebaba.config;

import java.util.Objects;
import java.util.Optional;

public class ServerConfiguration extends Configuration {
  private Optional<String> servicePackage = Optional.empty();

  private Optional<String> serverHandlerPackage = Optional.empty();

  public Optional<String> getServerHandlerPackage() {
    return serverHandlerPackage;
  }

  public Optional<String> getServicePackage() {
    return servicePackage;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ServerConfiguration that = (ServerConfiguration) obj;
    return Objects.equals(servicePackage, that.servicePackage)
        && Objects.equals(serverHandlerPackage, that.serverHandlerPackage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(servicePackage, serverHandlerPackage);
  }

  public static class ServerConfigurationBuilder {
    private ServerConfiguration serverConfiguration;

    private ServerConfigurationBuilder() {
      serverConfiguration = new ServerConfiguration();
    }

    public ServerConfigurationBuilder withServicePackage(Optional<String> servicePackage) {
      serverConfiguration.servicePackage = servicePackage;
      return this;
    }

    public ServerConfigurationBuilder withServerHandlerPackage(
		        Optional<String> serverHandlerPackage) {
      serverConfiguration.serverHandlerPackage = serverHandlerPackage;
      return this;
    }

    public ServerConfigurationBuilder withCodecPackage(Optional<String> codecPackage) {
      serverConfiguration.codecPackage = codecPackage;
      return this;
    }

    public static ServerConfigurationBuilder serverConfiguration() {
      return new ServerConfigurationBuilder();
    }

    public ServerConfiguration build() {
      return serverConfiguration;
    }
  }
}
