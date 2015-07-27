package io.grapebaba;

import org.junit.Test;

import io.grapebaba.codec.ProtocolsCodec;

public class ProtocolsCodecTest {

  @Test
  public void checkProtocolCodecRegistry() {
    ProtocolsCodec protocolsCodec = new ProtocolsCodec();
    System.out.println(ProtocolsCodec.codecRegistry.isEmpty());
  }
}
