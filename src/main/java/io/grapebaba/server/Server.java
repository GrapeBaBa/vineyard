package io.grapebaba.server;

import io.grapebaba.codec.ProtocolsCodec;
import io.grapebaba.codec.packet.PacketDecoder;
import io.grapebaba.codec.packet.PacketEncoder;
import io.grapebaba.config.ServerConfiguration;
import io.grapebaba.protocol.Protocol;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

/**
 * The default rpc server.
 */
public class Server {

  private static TcpServer<Protocol, Protocol> createServer(
        ServerConfiguration serverConfiguration) {
    return TcpServer.newServer(8076).pipelineConfigurator(
        channelPipeline -> {
        channelPipeline.addLast(PacketDecoder.class.getName(), new PacketDecoder());
        channelPipeline.addLast(ProtocolsCodec.class.getName(), new ProtocolsCodec(
              serverConfiguration));
        channelPipeline.addLast(new ServersHandler(serverConfiguration));
        channelPipeline.addLast(PacketEncoder.class.getName(), new PacketEncoder());
      });
  }
}
