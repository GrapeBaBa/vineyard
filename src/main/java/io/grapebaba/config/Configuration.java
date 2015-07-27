package io.grapebaba.config;

import java.util.Optional;

public class Configuration {
  public static final String INTERNAL_PACKAGE = "io.grapebaba";

  protected Optional<String> codecPackage = Optional.empty();

  public Optional<String> getCodecPackage() {
    return codecPackage;
  }
}
