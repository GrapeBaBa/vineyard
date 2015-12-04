package io.grapebaba.core;

import io.grapebaba.Service;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import io.reactivex.netty.protocol.tcp.server.events.TcpServerEventListener;
import io.reactivex.netty.protocol.tcp.server.events.TcpServerEventPublisher;
import io.reactivex.netty.protocol.tcp.ssl.SslCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.net.ssl.SSLEngine;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;


public class VineyardServerImpl<R,W> extends VineyardServer<R,W> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VineyardServerImpl.class);

    private final TcpServer<R, W> server;

    private VineyardServerImpl(TcpServer<R, W> server) {
        this.server = server;
    }

    @Override
    public <T> VineyardServer<R, W> channelOption(ChannelOption<T> option, T value) {
        return copy(server.channelOption(option,value));
    }

    @Override
    public <T> VineyardServer<R, W> clientChannelOption(ChannelOption<T> option, T value) {
        return copy(server.clientChannelOption(option,value));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerFirst(name,handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerFirst(group,name,handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerLast(String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerLast(name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerLast(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerLast(group, name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerBefore(baseName, name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerBefore(group, baseName, name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerAfter(baseName, name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.addChannelHandlerAfter(group, baseName, name, handlerFactory)));
    }

    @Override
    public <RR, WW> VineyardServer<RR, WW> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator) {
        return copy(VineyardServerImpl.<RR,WW>castServer(server.pipelineConfigurator(pipelineConfigurator)));
    }

    @Override
    public VineyardServer<R, W> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory) {
        return copy(server.secure(sslEngineFactory));
    }

    @Override
    public VineyardServer<R, W> secure(SSLEngine sslEngine) {
        return copy(server.secure(sslEngine));
    }

    @Override
    public VineyardServer<R, W> secure(SslCodec sslCodec) {
        return copy(server.secure(sslCodec));
    }

    @Override
    public VineyardServer<R, W> unsafeSecure() {
        return copy(server.unsafeSecure());
    }

    @Override
    public VineyardServer<R, W> enableWireLogging(LogLevel wireLoggingLevel) {
        return copy(server.enableWireLogging(wireLoggingLevel));
    }

    @Override
    public int getServerPort() {
        return server.getServerPort();
    }

    @Override
    public SocketAddress getServerAddress() {
        return server.getServerAddress();
    }

    @Override
    public VineyardServer<R, W> start(Service<R,W> service) {
        server.start(newConnection -> {
            Observable<W> result= newConnection.getInput().flatMap(service::call);
            return newConnection.writeAndFlushOnEach(result);
        });
        return this;
    }

    @Override
    public void shutdown() {
         server.shutdown();
    }

    @Override
    public void awaitShutdown() {
         server.awaitShutdown();
    }

    @Override
    public void awaitShutdown(long duration, TimeUnit timeUnit) {
         server.awaitShutdown(duration, timeUnit);
    }

    @Override
    public TcpServerEventPublisher getEventPublisher() {
        return server.getEventPublisher();
    }

    @Override
    public Subscription subscribe(TcpServerEventListener listener) {
        return server.subscribe(listener);
    }

    static VineyardServerImpl<ByteBuf, ByteBuf> create(final TcpServer<ByteBuf, ByteBuf> tcpServer) {
        return new VineyardServerImpl<>(tcpServer);
    }

    @SuppressWarnings("unchecked")
    private static <R,W> TcpServer<R, W> castServer(TcpServer<?, ?> rawTypes) {
        return (TcpServer<R,W>)rawTypes;
    }

    private static <RR, WW> VineyardServerImpl<RR, WW> copy(TcpServer<RR, WW> newServer) {
        return new VineyardServerImpl<>(newServer);
    }
}
