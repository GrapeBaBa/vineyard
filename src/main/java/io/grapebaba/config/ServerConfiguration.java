package io.grapebaba.config;

import java.util.Objects;

public class ServerConfiguration extends Configuration {
  private String servicePackages;

  private String serverHandlerPackages;

  public String getServerHandlerPackages() {
    return serverHandlerPackages;
  }

  public String getServicePackages() {
    return servicePackages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ServerConfiguration that = (ServerConfiguration) o;
    return Objects.equals(servicePackages, that.servicePackages)
        && Objects.equals(serverHandlerPackages, that.serverHandlerPackages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(servicePackages, serverHandlerPackages);
  }

  public static class ServerConfigurationBuilder {
    private ServerConfiguration serverConfiguration;

    private ServerConfigurationBuilder() {
      serverConfiguration = new ServerConfiguration();
    }

    public ServerConfigurationBuilder withServicePackages(String servicePackages) {
      serverConfiguration.servicePackages = servicePackages;
      return this;
    }

    public ServerConfigurationBuilder withServerHandlerPackages(String serverHandlerPackages) {
      serverConfiguration.serverHandlerPackages = serverHandlerPackages;
      return this;
    }

    public ServerConfigurationBuilder withCodecPackages(String codecPackages) {
      serverConfiguration.codecPackages = codecPackages;
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
