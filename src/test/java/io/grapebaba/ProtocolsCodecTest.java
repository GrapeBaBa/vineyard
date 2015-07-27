package io.grapebaba;

import io.grapebaba.codec.ProtocolsCodec;
import io.grapebaba.config.ServerConfiguration;

import org.junit.Assert;
import org.junit.Test;


public class ProtocolsCodecTest {

  @Test
  public void checkProtocolCodecRegistry() {
    ServerConfiguration serverConfiguration =
        ServerConfiguration.ServerConfigurationBuilder.serverConfiguration().build();
    ProtocolsCodec protocolsCodec = new ProtocolsCodec(serverConfiguration);
    Assert.assertTrue(protocolsCodec.getCodecRegistry().containsKey((byte) 0x76));
  }
}
