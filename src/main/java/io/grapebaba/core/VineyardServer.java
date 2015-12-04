package io.grapebaba.core;

import io.grapebaba.Service;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.reactivex.netty.events.EventSource;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import io.reactivex.netty.protocol.tcp.server.events.TcpServerEventListener;
import io.reactivex.netty.protocol.tcp.server.events.TcpServerEventPublisher;
import io.reactivex.netty.protocol.tcp.ssl.SslCodec;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.net.ssl.SSLEngine;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public abstract class VineyardServer<R,W> implements EventSource<TcpServerEventListener> {

    public abstract <T> VineyardServer<R, W> channelOption(ChannelOption<T> option, T value);

    public abstract <T> VineyardServer<R, W> clientChannelOption(ChannelOption<T> option, T value);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(EventExecutorGroup group, String name,
                                                                      Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW>  addChannelHandlerLast(String name,
                                                                      Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerLast(EventExecutorGroup group, String name,
                                                                     Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(String baseName, String name,
                                                                       Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(EventExecutorGroup group, String baseName,
                                                                       String name,
                                                                       Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(String baseName, String name,
                                                                      Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(EventExecutorGroup group, String baseName,
                                                                      String name, Func0<ChannelHandler> handlerFactory);

    public abstract <RR, WW> VineyardServer<RR, WW> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator);

    public abstract VineyardServer<R, W> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory);

    public abstract VineyardServer<R, W> secure(SSLEngine sslEngine);

    public abstract VineyardServer<R, W> secure(SslCodec sslCodec);

    public abstract VineyardServer<R, W> unsafeSecure();

    public abstract VineyardServer<R, W> enableWireLogging(LogLevel wireLoggingLevel);

    public abstract int getServerPort();

    public abstract SocketAddress getServerAddress();

    public abstract VineyardServer<R, W> start(Service<R,W> service);

    public abstract void shutdown();

    public abstract void awaitShutdown();

    public abstract void awaitShutdown(long duration, TimeUnit timeUnit);

    public abstract TcpServerEventPublisher getEventPublisher();

    public static VineyardServer<ByteBuf, ByteBuf> newServer() {
        return newServer(TcpServer.newServer(0));
    }

    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port) {
        return newServer(TcpServer.newServer(port));
    }

    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port, EventLoopGroup eventLoopGroup,
                                                         Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(port, eventLoopGroup, eventLoopGroup, channelClass));
    }


    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port, EventLoopGroup serverGroup,
                                                         EventLoopGroup clientGroup,
                                                         Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(port, serverGroup, clientGroup, channelClass));
    }

    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress) {
        return newServer(TcpServer.newServer(socketAddress));
    }

    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress, EventLoopGroup eventLoopGroup,
                                                         Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(socketAddress, eventLoopGroup, eventLoopGroup, channelClass));
    }

    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress, EventLoopGroup serverGroup,
                                                         EventLoopGroup clientGroup,
                                                         Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(socketAddress, serverGroup, clientGroup, channelClass));
    }

    private static VineyardServer<ByteBuf, ByteBuf> newServer(TcpServer<ByteBuf, ByteBuf> tcpServer) {
        return VineyardServerImpl.create(tcpServer);
    }
}
