package io.grapebaba.protocol.grapebaba;

import io.grapebaba.protocol.MessageType;

/**
 * Grapebaba protocol base class.
 */
public interface GrapebabaMessage {

    /**
     * Get message type.
     *
     * @return message type
     */
    MessageType getMessageType();

    /**
     * Get opaque.
     *
     * @return opaque
     */
    Integer getOpaque();
}
