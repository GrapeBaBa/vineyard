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

package io.grapebaba.common.serializer;

import io.grapebaba.common.protocol.SerializerType;

import java.util.EnumMap;

/**
 * The serializer factory.
 */
public class Serializers {
	private static final EnumMap<SerializerType, Serializer> map = new EnumMap<>(
			SerializerType.class);

	static {
		map.put(SerializerType.FST, new FastSerializer());
		map.put(SerializerType.KRYO, new KryoSerializer());
	}

	public static Serializer serializer(SerializerType serializerType) {
		return map.get(serializerType);
	}
}
