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

package io.grapebaba.vineyard.grape.protocol;

/**
 * The default protocol message type.
 */
public enum MessageType {
    /**
     * REQUEST.
     */
    REQUEST,
    /**
     * RESPONSE.
     */
    RESPONSE,
    /**
     * HEARTBEAT_REQUEST.
     */
    HEARTBEAT_REQUEST,
    /**
     * HEARTBEAT_REQUEST.
     */
    HEARTBEAT_RESPONSE;

    /**
     * Get byte of message type.
     *
     * @return byte of message type
     */
    public byte getValue() {
        switch (this) {
            case REQUEST:
                return 0x00;
            case RESPONSE:
                return 0x01;
            case HEARTBEAT_REQUEST:
                return 0x02;
            case HEARTBEAT_RESPONSE:
                return 0x03;
            default:
                throw new IllegalArgumentException("Unknown MessageType" + this);
        }

    }

    /**
     * Get message type.
     *
     * @param value value
     * @return message type
     */
    public static MessageType valueOf(byte value) {
        switch (value) {
            case 0x00:
                return REQUEST;
            case 0x01:
                return RESPONSE;
            case 0x02:
                return HEARTBEAT_REQUEST;
            case 0x03:
                return HEARTBEAT_RESPONSE;
            default:
                throw new IllegalArgumentException("Unknown MessageType " + value);
        }
    }
}
