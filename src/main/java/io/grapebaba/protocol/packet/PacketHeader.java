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

package io.grapebaba.protocol.packet;

import static com.google.common.base.MoreObjects.toStringHelper;

import io.grapebaba.protocol.Header;

import java.util.Objects;

/**
 * Data packet header structure.
 */
public class PacketHeader implements Header {
  private Integer bodyLength;

  public Integer getBodyLength() {
    return bodyLength;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PacketHeader packetHeader = (PacketHeader) obj;
    return Objects.equals(bodyLength, packetHeader.bodyLength);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bodyLength);
  }

  @Override
  public String toString() {
    return toStringHelper(this).add("bodyLength", bodyLength).toString();
  }

  public static final class HeaderBuilder {
    private PacketHeader packetHeader;

    private HeaderBuilder() {
      packetHeader = new PacketHeader();
    }

    public HeaderBuilder withBodyLength(Integer bodyLength) {
      packetHeader.bodyLength = bodyLength;
      return this;
    }

    public static HeaderBuilder header() {
      return new HeaderBuilder();
    }

    public PacketHeader build() {
      return packetHeader;
    }
  }
}
