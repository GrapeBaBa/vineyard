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

package io.grapebaba.vineyard.common.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * Metrics representation.
 */
public abstract class Metrics {
    /**
     * Registry for metrics.
     */
    public static final MetricRegistry REGISTRY = new MetricRegistry();

    private static final long DEFAULT_DURATION = 3L;

    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(REGISTRY).build();
        reporter.start(DEFAULT_DURATION, TimeUnit.SECONDS);
    }
}
