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

package io.grapebaba.vineyard.grape.heartbeat;

import io.grapebaba.vineyard.grape.codec.GrapeCodec;
import io.grapebaba.vineyard.grape.protocol.MessageType;
import io.grapebaba.vineyard.grape.protocol.grape.GrapeMessage;
import io.grapebaba.vineyard.grape.protocol.grape.HeartbeatResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The server side heartbeat handler.
 */
public class HeartbeatServerCodec
        extends MessageToMessageCodec<GrapeMessage, GrapeMessage> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HeartbeatClientCodec.class);

    private static final GrapeCodec CODEC = new GrapeCodec();

    @Override
    protected void encode(ChannelHandlerContext ctx, GrapeMessage msg,
                          List<Object> out) throws Exception {
        out.add(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, GrapeMessage msg, List<Object> out)
            throws Exception {
        if (msg.getMessageType() == MessageType.HEARTBEAT_REQUEST) {
            ctx.writeAndFlush(new HeartbeatResponseMessage());
        } else {
            out.add(msg);
        }
    }

}
