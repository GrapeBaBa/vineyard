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

package io.grapebaba.codec.vineyard;

import io.grapebaba.protocol.vineyard.VineyardMessage;
import io.grapebaba.protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The netty codec for VineyardMessage codec.
 */
public class VineyardCodecAdapter
        extends MessageToMessageCodec<Packet, VineyardMessage> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(VineyardCodecAdapter.class);

    private static final VineyardCodec CODEC = new VineyardCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, VineyardMessage msg,
                          List<Object> out) throws Exception {
        final ByteBuf payload = CODEC.encode(msg);
        final int payloadLength = payload.readableBytes();
        out.add(Packet.newBuilder().withBodyLength(payloadLength).withBodyByteBuf(payload)
                .build());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet msg, List<Object> out)
            throws Exception {
        out.add(CODEC.decode(msg.getBodyByteBuf()));
    }

}
