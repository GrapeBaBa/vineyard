package io.grapebaba.vineyard.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

public abstract class Metrics {
    public static final MetricRegistry REGISTRY = new MetricRegistry();

    static {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(REGISTRY).build();
        reporter.start(3L, TimeUnit.SECONDS);
    }
}
