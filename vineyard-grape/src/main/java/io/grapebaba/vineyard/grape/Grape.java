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
import io.grapebaba.vineyard.common.StackService;
import io.grapebaba.vineyard.common.client.VineyardClient;
import io.grapebaba.vineyard.common.codec.packet.PacketDecoder;
import io.grapebaba.vineyard.common.codec.packet.PacketEncoder;
import io.grapebaba.vineyard.common.protocol.packet.Packet;
import io.grapebaba.vineyard.common.server.VineyardServer;
import io.grapebaba.vineyard.common.metrics.StatFilter;
import io.grapebaba.vineyard.grape.codec.grape.GrapeCodecAdapter;
import io.grapebaba.vineyard.grape.metrics.GrapeStatFIlter;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import io.grapebaba.vineyard.grape.service.GrapeClientService;
import io.grapebaba.vineyard.grape.service.GrapeServerService;
import io.netty.buffer.ByteBuf;
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
    public static VineyardServer<RequestMessage, ResponseMessage> serve(SocketAddress socketAddress,
                                                                        Observable<Function> functionObservable) {
        return VineyardServer
                .newServer(socketAddress)
                .<ByteBuf, Packet>addChannelHandlerLast(PacketDecoder.class.getName(), PacketDecoder::new)
                .<Packet, ByteBuf>addChannelHandlerLast(PacketEncoder.class.getName(), PacketEncoder::new)
                .<RequestMessage, ResponseMessage>addChannelHandlerLast(GrapeCodecAdapter.class.getName(),
                        GrapeCodecAdapter::new)
                .start(new GrapeServerService(functionObservable));
    }

    /**
     * Return a client object.
     *
     * @param socketAddresses input
     * @return a client
     */
    @SuppressWarnings("unchecked")
    public static Service<RequestMessage, ResponseMessage> newClient(
            SocketAddress... socketAddresses) {
        return VineyardClient.newClient(socketAddresses)
                .<ByteBuf, Packet>addChannelHandlerLast(PacketDecoder.class.getName(),
                        PacketDecoder::new)
                .<Packet, ByteBuf>addChannelHandlerLast(PacketEncoder.class.getName(),
                        PacketEncoder::new)
                .<RequestMessage, ResponseMessage>addChannelHandlerLast(GrapeCodecAdapter.class.getName(),
                        GrapeCodecAdapter::new)
                .createService(client ->
                        new StackService<>(just(new StatFilter<>(), new GrapeStatFIlter()),
                                new GrapeClientService(client)));
    }
}
