package io.grapebaba.protocol;

public interface Protocol<H extends Header, B extends Body> {
  H header();

  B body();

  byte getMagicNumber();

}
