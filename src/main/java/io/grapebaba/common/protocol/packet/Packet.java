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

package io.grapebaba.common.protocol.packet;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;

import java.util.Objects;

/**
 * The data packet structure.
 */
public class Packet {
	private ByteBuf bodyByteBuf;

	private Integer bodyLength;

	private Packet(Builder builder) {
		bodyByteBuf = builder.bodyByteBuf;
		bodyLength = builder.bodyLength;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public ByteBuf getBodyByteBuf() {
		return bodyByteBuf;
	}

	public Integer getBodyLength() {
		return bodyLength;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Packet packet = (Packet) o;
		return Objects.equals(bodyByteBuf, packet.bodyByteBuf)
				&& Objects.equals(bodyLength, packet.bodyLength);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bodyByteBuf, bodyLength);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("bodyByteBuf", bodyByteBuf)
				.add("bodyLength", bodyLength).toString();
	}

	public static final class Builder {
		private ByteBuf bodyByteBuf;
		private Integer bodyLength;

		private Builder() {
		}

		public Builder withBodyByteBuf(ByteBuf bodyByteBuf) {
			this.bodyByteBuf = bodyByteBuf;
			return this;
		}

		public Builder withBodyLength(Integer bodyLength) {
			this.bodyLength = bodyLength;
			return this;
		}

		public Packet build() {
			return new Packet(this);
		}
	}
}
