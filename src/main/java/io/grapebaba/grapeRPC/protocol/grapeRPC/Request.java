package io.grapebaba.grapeRPC.protocol.grapeRPC;

import io.grapebaba.grapeRPC.serializer.SerializerType;

public class Request {
    public static final byte MAGIC_NUMBER = (byte) 0x80;

    private int version;

    private Integer opaque;

    private MessageType messageType;

    private SerializerType serializerType;

    private String beanName;

    private String methodName;

    private Object[] arguments;

}
