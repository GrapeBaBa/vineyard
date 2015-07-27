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

package io.grapebaba.protocol;

public enum Version {
  V1;

  /**
   * Get byte of version.
   * 
   * @return byte of version
   */
  public byte getValue() {
    switch (this) {
      case V1:
        return 0x00;
      default:
        throw new IllegalArgumentException("Unknown Version" + this);
    }

  }

  /**
   * Get version.
   * 
   * @param value value
   * @return version
   */
  public static Version valueOf(byte value) {
    switch (value) {
      case 0x00:
        return V1;
      default:
        throw new IllegalArgumentException("Unknown Version " + value);
    }
  }
}
