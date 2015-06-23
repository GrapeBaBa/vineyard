package io.grapebaba.protocol;

public interface Protocol<H extends Header, B extends Body> {
    byte MAGIC_NUMBER = (byte) 0x76;

    H header();

    B body();

}
