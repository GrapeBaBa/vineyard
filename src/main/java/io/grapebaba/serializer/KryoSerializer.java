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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.pool.KryoPool.Builder;

/**
 * The serializer implemented by Kryo library.
 */
public class KryoSerializer implements Serializer {
    private static final KryoPool KRYO_POOL = new Builder(() -> {
        Kryo kryo = new Kryo();
        // configure kryo instance, customize settings
        return kryo;
    }).softReferences().build();

    /**
     * Serialize the object.
     *
     * @param obj obj
     * @return byte array
     */
    public byte[] serialize(Object obj) {
        final int defaultBufferSize = 4096;
        return KRYO_POOL.run(kryo -> {
            Output output = new Output(defaultBufferSize);
            kryo.writeClassAndObject(output, obj);
            return output.getBuffer();
        });
    }

    /**
     * Deserialize.
     *
     * @param bytes input
     * @return object
     */
    public Object deserialize(byte[] bytes) {
        return KRYO_POOL.run(kryo -> kryo.readClassAndObject(new Input(bytes)));
    }
}
