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

package io.grapebaba.vineyard.grape.codec;

import io.grapebaba.vineyard.grape.heartbeat.HeartbeatClientCodec;
import io.grapebaba.vineyard.grape.protocol.grape.GrapeMessage;
import io.grapebaba.vineyard.common.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.grapebaba.vineyard.common.protocol.packet.Packet.newBuilder;

/**
 * The netty codec for GrapeMessage codec.
 */
public class GrapeCodecAdapter
        extends MessageToMessageCodec<Packet, GrapeMessage> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HeartbeatClientCodec.class);

    private static final GrapeCodec CODEC = new GrapeCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, GrapeMessage msg,
                          List<Object> out) throws Exception {
        final ByteBuf payload = CODEC.encode(msg);
        final int payloadLength = payload.readableBytes();
        out.add(newBuilder().withBodyLength(payloadLength).withBodyByteBuf(payload)
                .build());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet msg, List<Object> out)
            throws Exception {
        out.add(CODEC.decode(msg.getBodyByteBuf()));
    }

}
