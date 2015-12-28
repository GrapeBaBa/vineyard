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

package io.grapebaba.vineyard.common.serializer;

/**
 * The default serializer type.
 */
public enum SerializerType {
    /**
     * DEFAULT Serializer lib.
     */
    JAVA,
    /**
     * FAST Serializer lib.
     */
    FST,
    /**
     * KRYO Serializer lib.
     */
    KRYO;

    /**
     * Get byte of serializer type.
     *
     * @return byte of serializer type
     */
    public byte getValue() {
        switch (this) {
            case JAVA:
                return 0x00;
            case KRYO:
                return 0x01;
            case FST:
                return 0x02;
            default:
                throw new IllegalArgumentException("Unknown SerializerType" + this);
        }

    }

    /**
     * Get serializer type.
     *
     * @param value value
     * @return serializer type
     */
    public static SerializerType valueOf(byte value) {
        switch (value) {
            case 0x00:
                return JAVA;
            case 0x01:
                return KRYO;
            case 0x02:
                return FST;
            default:
                throw new IllegalArgumentException("Unknown SerializerType" + value);
        }
    }
}
