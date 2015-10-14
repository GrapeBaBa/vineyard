package io.grapebaba.common.protocol;

public interface PipliningSupportedProtocol<H, B> extends Protocol<H, B> {
  Integer getOpaque();
}
