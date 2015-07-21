package io.grapebaba.serializer;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FSTSerializer implements Serializer{

  private static FSTConfiguration fstConfiguration = FSTConfiguration.createDefaultConfiguration();

  public byte[] serialize(Object obj) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    FSTObjectOutput out = fstConfiguration.getObjectOutput(outputStream);
    try {
      out.writeObject(obj);
      out.flush();
      outputStream.close();

      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Object deserialize(byte[] bytes) {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    FSTObjectInput input = fstConfiguration.getObjectInput(inputStream);

    try {
      Object obj = input.readObject();
      inputStream.close();

      return obj;
    } catch (ClassNotFoundException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
