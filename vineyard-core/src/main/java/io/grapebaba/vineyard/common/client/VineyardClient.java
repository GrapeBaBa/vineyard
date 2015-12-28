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

package io.grapebaba.vineyard.common.client;

import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.ServiceFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.reactivex.netty.events.EventSource;
import io.reactivex.netty.protocol.tcp.client.ConnectionProvider;
import io.reactivex.netty.protocol.tcp.client.ConnectionRequest;
import io.reactivex.netty.protocol.tcp.client.events.TcpClientEventListener;
import io.reactivex.netty.protocol.tcp.ssl.SslCodec;
import netflix.ocelli.rxnetty.protocol.tcp.TcpLoadBalancer;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.net.ssl.SSLEngine;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import static netflix.ocelli.Instance.create;
import static rx.Observable.*;

/**
 * A tcp client wrapper.
 *
 * @param <W>
 * @param <R>
 */
public abstract class VineyardClient<W, R> implements EventSource<TcpClientEventListener> {
    public abstract Service<W, R> createService(ServiceFactory<W, R> serviceFactory);

    public abstract ConnectionRequest<W, R> createConnectionRequest();

    public abstract <T> VineyardClient<W, R> channelOption(ChannelOption<T> option, T value);

    public abstract VineyardClient<W, R> readTimeOut(int timeOut, TimeUnit timeUnit);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator);

    public abstract VineyardClient<W, R> enableWireLogging(LogLevel wireLoggingLevel);

    public abstract VineyardClient<W, R> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory);

    public abstract VineyardClient<W, R> secure(SSLEngine sslEngine);

    public abstract VineyardClient<W, R> secure(SslCodec sslCodec);

    public abstract VineyardClient<W, R> unsafeSecure();

    public static VineyardClient<ByteBuf, ByteBuf> newClient(ConnectionProvider<ByteBuf, ByteBuf> connectionProvider) {
        return VineyardClientImpl.create(connectionProvider);
    }

    public static VineyardClient<ByteBuf, ByteBuf> newClient(SocketAddress... socketAddresses) {
        return VineyardClient
                .newClient(TcpLoadBalancer.<ByteBuf, ByteBuf>roundRobin(
                        from(socketAddresses).flatMap(
                                socketAddress -> just(create(socketAddress,
                                        never())))).toConnectionProvider());
    }
}
