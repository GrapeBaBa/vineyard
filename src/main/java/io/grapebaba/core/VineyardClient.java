package io.grapebaba.core;

import io.grapebaba.Service;
import io.grapebaba.ServiceFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.reactivex.netty.events.EventSource;
import io.reactivex.netty.protocol.tcp.client.events.TcpClientEventListener;
import io.reactivex.netty.protocol.tcp.ssl.SslCodec;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.TimeUnit;

public abstract class VineyardClient<W,R> implements EventSource<TcpClientEventListener> {
    public abstract Service<W, R> createService(ServiceFactory<W,R> serviceFactory) ;

    public abstract  <T> VineyardClient<W, R> channelOption(ChannelOption<T> option, T value);

    public abstract VineyardClient<W, R> readTimeOut(int timeOut, TimeUnit timeUnit);

    public abstract  <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerFirst(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerLast(EventExecutorGroup group, String name, Func0<ChannelHandler> handlerFactory);

    public abstract  <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerBefore(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> addChannelHandlerAfter(EventExecutorGroup group, String baseName, String name, Func0<ChannelHandler> handlerFactory);

    public abstract <WW, RR> VineyardClient<WW, RR> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator);

    public abstract VineyardClient<W, R> enableWireLogging(LogLevel wireLoggingLevel);

    public abstract VineyardClient<W, R> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory);

    public abstract VineyardClient<W, R> secure(SSLEngine sslEngine);

    public abstract VineyardClient<W, R> secure(SslCodec sslCodec);

    public abstract VineyardClient<W, R> unsafeSecure();

}
