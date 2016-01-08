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

package io.grapebaba.vineyard.common.server;

import io.grapebaba.vineyard.common.Service;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
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

/**
 * A tcp server wrapper.
 *
 * @param <R>
 * @param <W>
 */
public abstract class VineyardServer<R, W> implements EventSource<TcpServerEventListener> {

    /**
     * Creates a new server instances.
     *
     * @param option option
     * @param value  value for the option
     * @return a new server instance
     */
    public abstract <T> VineyardServer<R, W> channelOption(ChannelOption<T> option, T value);

    /**
     * Creates a new server instances.
     *
     * @param option option
     * @param value  value for the option
     * @return a new server instance
     */
    public abstract <T> VineyardServer<R, W> clientChannelOption(ChannelOption<T> option, T value);

    /**
     * Adds a channelHandler.
     *
     * @param name           name of the handler
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(String name, Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param group          the EventExecutorGroup
     * @param name           the name of the handler to append
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerFirst(EventExecutorGroup group, String name,
                                                                           Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param name           name of the handler
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerLast(String name,
                                                                          Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param group          the EventExecutorGroup
     * @param name           the name of the handler to append
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerLast(EventExecutorGroup group, String name,
                                                                          Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param baseName       the name of the existing handler
     * @param name           name of the handler
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(String baseName, String name,
                                                                            Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param group          the EventExecutorGroup
     * @param baseName       the name of the existing handler
     * @param name           the name of the handler to append
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerBefore(EventExecutorGroup group, String baseName,
                                                                            String name,
                                                                            Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param baseName       the name of the existing handler
     * @param name           name of the handler.
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(String baseName, String name,
                                                                           Func0<ChannelHandler> handlerFactory);

    /**
     * Adds a channelHandler.
     *
     * @param group          the EventExecutorGroup
     * @param baseName       the name of the existing handler
     * @param name           the name of the handler to append
     * @param handlerFactory factory to create handler instance to add
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> addChannelHandlerAfter(EventExecutorGroup group, String baseName,
                                                                           String name, Func0<ChannelHandler> handlerFactory);

    /**
     * Creates a new server instances.
     *
     * @param pipelineConfigurator Action to configure {@link ChannelPipeline}
     * @return a new server instance
     */
    public abstract <RR, WW> VineyardServer<RR, WW> pipelineConfigurator(Action1<ChannelPipeline> pipelineConfigurator);

    /**
     * Creates a new server instance.
     *
     * @param sslEngineFactory factory for all secured connections created by the newly created client instance.
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> secure(Func1<ByteBufAllocator, SSLEngine> sslEngineFactory);

    /**
     * Creates a new server instance.
     *
     * @param sslEngine for all secured connections created by the newly created client instance.
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> secure(SSLEngine sslEngine);

    /**
     * Creates a new server instance.
     *
     * @param sslCodec for all secured connections created by the newly created client instance
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> secure(SslCodec sslCodec);

    /**
     * Creates a new server instance.
     * This is only for testing and should not be used for real production clients.
     *
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> unsafeSecure();

    /**
     * Creates a new server instances.
     *
     * @param wireLoggingLevel logging level at which the wire logs will be logged
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> enableWireLogging(LogLevel wireLoggingLevel);

    /**
     * Returns the port at which this server is running.
     * <p>
     * For servers using ephemeral ports, this would return the actual port used, only after the server is started.
     *
     * @return The port at which this server is running.
     */
    public abstract int getServerPort();

    /**
     * Returns the address at which this server is running.
     *
     * @return The address at which this server is running.
     */
    public abstract SocketAddress getServerAddress();

    /**
     * Create a server instance using a service.
     *
     * @param service service
     * @return a new server instance
     */
    public abstract VineyardServer<R, W> start(Service<R, W> service);

    /**
     * Shutdown this server and waits till the server socket is closed.
     */
    public abstract void shutdown();

    /**
     * Waits for the shutdown of this server.
     * <p>
     * <b>This does not actually shutdown the server.</b> It just waits for some other action to shutdown.
     */
    public abstract void awaitShutdown();

    /**
     * Waits for the shutdown of this server, waiting a maximum of the passed duration.
     * <p>
     * <b>This does not actually shutdown the server.</b> It just waits for some other action to shutdown.
     *
     * @param duration Duration to wait for shutdown.
     * @param timeUnit Timeunit for the duration to wait for shutdown.
     */
    public abstract void awaitShutdown(long duration, TimeUnit timeUnit);

    /**
     * Returns the event publisher for this server.
     *
     * @return The event publisher for this server.
     */
    public abstract TcpServerEventPublisher getEventPublisher();

    /**
     * Creates a new server using an ephemeral port. The port used can be queried after starting this server, using
     * {@link #getServerPort()}
     *
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer() {
        return newServer(TcpServer.newServer(0));
    }

    /**
     * Creates a new server using the passed port.
     *
     * @param port Port for the server. {@code 0} to use ephemeral port.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port) {
        return newServer(TcpServer.newServer(port));
    }

    /**
     * Creates a new server using the passed port.
     *
     * @param port           Port for the server. {@code 0} to use ephemeral port.
     * @param eventLoopGroup Eventloop group to be used for server as well as client sockets.
     * @param channelClass   The class to be used for server channel.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port, EventLoopGroup eventLoopGroup,
                                                             Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(port, eventLoopGroup, eventLoopGroup, channelClass));
    }

    /**
     * Creates a new server using the passed port.
     *
     * @param port         Port for the server. {@code 0} to use ephemeral port.
     * @param serverGroup  Eventloop group to be used for server sockets.
     * @param clientGroup  Eventloop group to be used for client sockets.
     * @param channelClass The class to be used for server channel.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(int port, EventLoopGroup serverGroup,
                                                             EventLoopGroup clientGroup,
                                                             Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(port, serverGroup, clientGroup, channelClass));
    }

    /**
     * Creates a new server using the passed address.
     *
     * @param socketAddress Socket address for the server.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress) {
        return newServer(TcpServer.newServer(socketAddress));
    }

    /**
     * Creates a new server using the passed address.
     *
     * @param socketAddress  Socket address for the server.
     * @param eventLoopGroup Eventloop group to be used for server as well as client sockets.
     * @param channelClass   The class to be used for server channel.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress, EventLoopGroup eventLoopGroup,
                                                             Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(socketAddress, eventLoopGroup, eventLoopGroup, channelClass));
    }

    /**
     * Creates a new server using the passed address.
     *
     * @param socketAddress Socket address for the server.
     * @param serverGroup   Eventloop group to be used for server sockets.
     * @param clientGroup   Eventloop group to be used for client sockets.
     * @param channelClass  The class to be used for server channel.
     * @return A new server instance
     */
    public static VineyardServer<ByteBuf, ByteBuf> newServer(SocketAddress socketAddress, EventLoopGroup serverGroup,
                                                             EventLoopGroup clientGroup,
                                                             Class<? extends ServerChannel> channelClass) {
        return newServer(TcpServer.newServer(socketAddress, serverGroup, clientGroup, channelClass));
    }

    private static VineyardServer<ByteBuf, ByteBuf> newServer(TcpServer<ByteBuf, ByteBuf> tcpServer) {
        return VineyardServerImpl.create(tcpServer);
    }
}
