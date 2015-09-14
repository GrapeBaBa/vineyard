/*
 * Copyright 2015 281165273grape@gmail.com
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.grapebaba.server;

import io.grapebaba.common.codec.ProtocolsCodec;
import io.grapebaba.common.codec.packet.PacketDecoder;
import io.grapebaba.common.codec.packet.PacketEncoder;
import io.grapebaba.server.config.ServerConfiguration;
import io.grapebaba.common.protocol.Protocol;
import io.grapebaba.server.handler.ServersHandler;
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
