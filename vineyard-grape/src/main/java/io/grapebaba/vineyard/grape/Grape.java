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

package io.grapebaba.vineyard.grape;

import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.codec.packet.PacketDecoder;
import io.grapebaba.vineyard.common.codec.packet.PacketEncoder;
import io.grapebaba.vineyard.common.StackService;
import io.grapebaba.vineyard.common.metrics.StatFilter;
import io.grapebaba.vineyard.grape.codec.GrapeCodecAdapter;
//import io.grapebaba.vineyard.grape.heartbeat.HeartbeatClientCodec;
//import io.grapebaba.vineyard.grape.heartbeat.HeartbeatClientHandler;
//import io.grapebaba.vineyard.grape.heartbeat.HeartbeatServerCodec;
//import io.grapebaba.vineyard.grape.heartbeat.HeartbeatServerHandler;
import io.grapebaba.vineyard.grape.metrics.GrapeStatFilter;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import io.grapebaba.vineyard.grape.service.GrapeClientService;
import io.grapebaba.vineyard.grape.service.GrapeServerService;
//import io.netty.handler.timeout.IdleStateHandler;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Function;

import java.net.SocketAddress;

import static rx.Observable.just;

/**
 * Grape protocol representation.
 */
public abstract class Grape {
    private static final Logger LOGGER = LoggerFactory.getLogger(Grape.class);

    /**
     * Return a server object.
     *
     * @param socketAddress      A socket address
     * @param functionObservable A set of function objects
     * @return tcpServer
     */
    @SuppressWarnings({"rawtypes"})
    public static TcpServer<RequestMessage, ResponseMessage> serve(SocketAddress socketAddress,
                                                                   Observable<Function> functionObservable) {
        final int defaultIdleTime = 60;
        final Service<RequestMessage, ResponseMessage> service = new GrapeServerService(functionObservable);

        return TcpServer
                .newServer(socketAddress)
                .addChannelHandlerLast(PacketDecoder.class.getName(), PacketDecoder::new)
                .addChannelHandlerLast(PacketEncoder.class.getName(), PacketEncoder::new)
                .<RequestMessage, ResponseMessage>addChannelHandlerLast(GrapeCodecAdapter.class.getName(),
                        GrapeCodecAdapter::new)
//                .addChannelHandlerLast(HeartbeatServerCodec.class.getName(),
//                        HeartbeatServerCodec::new)
//                .addChannelHandlerLast(
//                        IdleStateHandler.class.getName(),
//                        () -> new IdleStateHandler(defaultIdleTime, defaultIdleTime, defaultIdleTime))
//                .<RequestMessage, ResponseMessage>addChannelHandlerLast(
//                        HeartbeatServerHandler.class.getName(), HeartbeatServerHandler::new)
                .start(newConnection -> {
                    Observable<ResponseMessage> result = newConnection.getInput().flatMap(service::call);
                    return newConnection.writeAndFlushOnEach(result);
                });
    }

    /**
     * Return a client object.
     *
     * @param socketAddress input
     * @return a client
     */
    @SuppressWarnings("unchecked")
    public static Service<RequestMessage, ResponseMessage> newClient(
            SocketAddress socketAddress) {
        final int defaultIdleTime = 30;
        //final int defaultMaxConnectionNumer = 4;

        TcpClient<RequestMessage, ResponseMessage> client = TcpClient.newClient(
                socketAddress)
                .addChannelHandlerLast(PacketDecoder.class.getName(),
                        PacketDecoder::new)
                .addChannelHandlerLast(PacketEncoder.class.getName(),
                        PacketEncoder::new)
                .<RequestMessage, ResponseMessage>addChannelHandlerLast(GrapeCodecAdapter.class.getName(),
                        GrapeCodecAdapter::new);
//                .addChannelHandlerLast(
//                        HeartbeatClientCodec.class.getName(), HeartbeatClientCodec::new)
//                .addChannelHandlerLast(
//                        IdleStateHandler.class.getName(),
//                        () -> new IdleStateHandler(
//                                defaultIdleTime, defaultIdleTime, defaultIdleTime))
//                .<RequestMessage, ResponseMessage>addChannelHandlerLast(
//                        HeartbeatClientHandler.class.getName(), HeartbeatClientHandler::new);

        return new StackService<>(just(new StatFilter<>(), new GrapeStatFilter()),
                new GrapeClientService(client));
    }
}
