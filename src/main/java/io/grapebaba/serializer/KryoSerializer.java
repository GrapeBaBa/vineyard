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
  private static final KryoPool kryoPool = new Builder(() -> {
      Kryo kryo = new Kryo();
    // configure kryo instance, customize settings
      return kryo;
    }).softReferences().build();

  /**
   * Serialize the object.
   * @param obj obj
   * @return byte array
   */
  public byte[] serialize(Object obj) {
    return kryoPool.run(kryo -> {
        Output output = new Output();
        kryo.writeClassAndObject(output, obj);
        return output.getBuffer();
      });
  }

  public Object deserialize(byte[] bytes) {
    return kryoPool.run(kryo -> kryo.readClassAndObject(new Input(bytes)));
  }
}
