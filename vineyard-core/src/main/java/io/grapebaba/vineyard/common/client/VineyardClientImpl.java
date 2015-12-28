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
import io.reactivex.netty.protocol.tcp.client.ConnectionProvider;
import io.reactivex.netty.protocol.tcp.client.ConnectionRequest;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import io.reactivex.netty.protocol.tcp.client.TcpClientImpl;
import io.reactivex.netty.protocol.tcp.client.events.TcpClientEventListener;
import io.reactivex.netty.protocol.tcp.ssl.SslCodec;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.TimeUnit;

/**
 * Tcp client implementation for transport.
 *
 * @param <W>
 * @param <R>
 */
public class VineyardClientImpl<W,R> extends VineyardClient<W,R> {
    private final TcpClient<W, R> client;

    private VineyardClientImpl(TcpClient<W, R> client) {
        this.client = client;
    }

    @Override
    public Service<W, R> createService(ServiceFactory<W,R> serviceFactory) {
        return serviceFactory.create(this);
    }

    @Override
    public ConnectionRequest<W, R> createConnectionRequest() {
        return client.createConnectionRequest();
    }

    @Override
    public <T> VineyardClient<W, R> channelOption(ChannelOption<T> option, T value) {
        return copy(client.channelOption(option,value));
    }

    @Override
    public VineyardClient<W, R> readTimeOut(int timeOut, TimeUnit timeUnit) {
        return copy(client.readTimeOut(timeOut, timeUnit));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerFirst(name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerFirst(group, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerLast(name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerLast(group, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerBefore(baseName, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerBefore(group, baseName, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerAfter(baseName, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardClientImpl.castClient(client.addChannelHandlerAfter(group, baseName, name, handlerFactory)));
    }

    @Override
    public <WW, RR> VineyardClient<WW, RR> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator) {
        return copy(VineyardClientImpl.castClient(client.pipelineConfigurator(pipelineConfigurator)));
    }

    @Override
    public VineyardClient<W, R> enableWireLogging(LogLevel wireLoggingLevel) {
        return copy(client.enableWireLogging(wireLoggingLevel));
    }

    @Override
    public VineyardClient<W, R> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory) {
        return copy(client.secure(sslEngineFactory));
    }

    @Override
    public VineyardClient<W, R> secure(SSLEngine sslEngine) {
        return copy(client.secure(sslEngine));
    }

    @Override
    public VineyardClient<W, R> secure(SslCodec sslCodec) {
        return copy(client.secure(sslCodec));
    }

    @Override
    public VineyardClient<W, R> unsafeSecure() {
        return copy(client.unsafeSecure());
    }

    @Override
    public Subscription subscribe(TcpClientEventListener listener) {
        return client.subscribe(listener);
    }

    public static VineyardClient<ByteBuf, ByteBuf> create(final ConnectionProvider<ByteBuf, ByteBuf> connectionProvider) {
        TcpClient<ByteBuf, ByteBuf> tcpClient = TcpClientImpl.create(connectionProvider);
        return new VineyardClientImpl<>(tcpClient);
    }

    @SuppressWarnings("unchecked")
    private static <WW,RR> TcpClient<WW, RR> castClient(TcpClient<?, ?> rawTypes) {
        return (TcpClient<WW,RR>) rawTypes;
    }

    private <WW, RR> VineyardClientImpl<WW, RR> copy(TcpClient<WW, RR> newClient) {
        return new VineyardClientImpl<>(newClient);
    }
}
