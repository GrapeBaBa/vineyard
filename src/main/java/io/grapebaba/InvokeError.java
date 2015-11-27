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

package io.grapebaba;

import java.io.Serializable;

/**
 * A common exception for invoking.
 */
public class InvokeError implements Serializable {
    private String msg;

    /**
     * Default constructor.
     */
    public InvokeError() {
        super();
    }

    /**
     * Constructor with error message.
     */
    public InvokeError(String msg) {
        this.msg = msg;
    }

    /**
     * Get error message.
     *
     * @return message
     */
    public String getMsg() {
        return msg;
    }
}
