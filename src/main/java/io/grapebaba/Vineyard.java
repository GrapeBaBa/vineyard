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

package io.grapebaba;

import io.grapebaba.codec.vineyard.VineyardCodecAdapter;
import io.grapebaba.codec.packet.PacketDecoder;
import io.grapebaba.codec.packet.PacketEncoder;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.vineyard.RequestMessage;
import io.grapebaba.protocol.vineyard.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import netflix.ocelli.rxnetty.protocol.tcp.TcpLoadBalancer;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Single;
import rx.functions.Function;
import rx.subjects.ReplaySubject;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Throwables.getRootCause;
import static netflix.ocelli.Instance.create;
import static rx.Observable.from;
import static rx.Observable.just;
import static rx.Observable.never;


/**
 * Vineyard protocol representation.
 */
public abstract class Vineyard {
    private static final Logger LOGGER = LoggerFactory.getLogger(Vineyard.class);

    /**
     * Return a server object.
     *
     * @param socketAddress      A socket address
     * @param functionObservable A set of function objects
     * @return tcpServer
     */
    public static TcpServer serve(SocketAddress socketAddress,
                                  Observable<Function> functionObservable) {
        return TcpServer
                .newServer(socketAddress)
                .addChannelHandlerLast(PacketDecoder.class.getName(), PacketDecoder::new)
                .addChannelHandlerLast(PacketEncoder.class.getName(), PacketEncoder::new)
                .addChannelHandlerLast(VineyardCodecAdapter.class.getName(),
                        VineyardCodecAdapter::new)
                .start(newConnection -> newConnection
                        .getInput()
                        .flatMap(
                                message -> {
                                    final RequestMessage requestMessage = (RequestMessage) message;
                                    final String beanName = requestMessage.getBeanName();

                                    return functionObservable
                                            .first(function -> function.getClass()
                                                    .getName().equals(beanName))
                                            .flatMap(
                                                    function -> {
                                                        Object result;
                                                        try {
                                                            result = MethodUtils
                                                                    .invokeMethod(
                                                                            function,
                                                                            requestMessage
                                                                                    .getMethodName(),
                                                                            requestMessage
                                                                                    .getArguments());
                                                        } catch (Throwable e) {
                                                            LOGGER.error(
                                                                    "Invoke service method exception",
                                                                    e);
                                                            Throwable root = getRootCause(e);
                                                            result = new InvokeError(
                                                                    null == root
                                                                            ? "Cannot find root cause exception"
                                                                            : root.getClass().getName()
                                                                            + ":" + root.getMessage());
                                                        }

                                                        ResponseMessage responseMessage = ResponseMessage
                                                                .newBuilder()
                                                                .withMessageType(MessageType.RESPONSE)
                                                                .withSerializerType(requestMessage
                                                                        .getSerializerType())
                                                                .withOpaque(requestMessage.getOpaque())
                                                                .withResult(result)
                                                                .build();

                                                        return newConnection
                                                                .writeAndFlushOnEach(just(responseMessage));
                                                    });
                                }));
    }

    /**
     * Return a client object.
     *
     * @param socketAddresses input
     * @return a client
     */
    public static Service<RequestMessage, ResponseMessage> newClient(
            SocketAddress... socketAddresses) {
        return new Service<RequestMessage, ResponseMessage>() {
            private final ConcurrentMap<Integer, ReplaySubject<ResponseMessage>> req2Res = new ConcurrentHashMap<>();

            private final TcpClient<RequestMessage, ResponseMessage> client = TcpClient
                    .newClient(
                            TcpLoadBalancer.<ByteBuf, ByteBuf>roundRobin(
                                    from(socketAddresses).flatMap(
                                            socketAddress -> just(create(socketAddress,
                                                    never())))).toConnectionProvider())
                    .addChannelHandlerLast(PacketDecoder.class.getName(),
                            PacketDecoder::new)
                    .addChannelHandlerLast(PacketEncoder.class.getName(),
                            PacketEncoder::new)
                    .addChannelHandlerLast(VineyardCodecAdapter.class.getName(),
                            VineyardCodecAdapter::new);

            @Override
            public Single<ResponseMessage> call(RequestMessage message) {
                ReplaySubject<ResponseMessage> response = ReplaySubject.create();
                req2Res.putIfAbsent(message.getOpaque(), response);

                client.createConnectionRequest()
                        .flatMap(
                                connection -> connection.write(Observable.just(message))
                                        .cast(ResponseMessage.class)
                                        .mergeWith(connection.getInput()))
                        .subscribe(
                                responseMessage -> {
                                    ReplaySubject<ResponseMessage> responseMessageReplaySubject = req2Res
                                            .get(message.getOpaque());
                                    responseMessageReplaySubject.onNext(responseMessage);
                                    responseMessageReplaySubject.onCompleted();
                                });

                return response.toSingle();
            }
        };
    }
}
