package io.grapebaba.common.protocol;

public interface PipeliningSupportedProtocol<H, B> extends Protocol<H, B> {
  Integer getOpaque();
}
