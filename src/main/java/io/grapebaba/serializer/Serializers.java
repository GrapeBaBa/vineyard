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

package io.grapebaba.serializer;

import io.grapebaba.protocol.SerializerType;

import java.util.EnumMap;

/**
 * The serializer factory.
 */
public abstract class Serializers {
    private static final EnumMap<SerializerType, Serializer> SERIALIZER_ENUM_MAP = new EnumMap<>(
            SerializerType.class);

    static {
        SERIALIZER_ENUM_MAP.put(SerializerType.JAVA, new JavaSerializer());
        SERIALIZER_ENUM_MAP.put(SerializerType.FST, new FastSerializer());
        SERIALIZER_ENUM_MAP.put(SerializerType.KRYO, new KryoSerializer());
    }

    /**
     * Determine serializer by type.
     *
     * @param serializerType input
     * @return serializer
     */
    public static Serializer serializer(SerializerType serializerType) {
        return SERIALIZER_ENUM_MAP.get(serializerType);
    }
}
