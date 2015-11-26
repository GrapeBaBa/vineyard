package io.grapebaba.common.protocol.grapebaba;

import io.grapebaba.common.protocol.MessageType;

public abstract class GrapebabaMessage {
	protected Integer opaque;

	public abstract MessageType getMessageType();

	public Integer getOpaque() {
		return opaque;
	}
}
