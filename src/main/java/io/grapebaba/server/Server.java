package io.grapebaba.server;

import io.grapebaba.ProtocolsCodec;
import io.grapebaba.protocol.Protocol;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

public class Server {

    private static final TcpServer<Protocol, Protocol> server =
            TcpServer.newServer(8076)
                    .pipelineConfigurator(channelPipeline -> {
                        channelPipeline.addLast(ProtocolsCodec.class.getName(), new ProtocolsCodec());
                        channelPipeline.addLast(new ServersHandler());
                    });
}
