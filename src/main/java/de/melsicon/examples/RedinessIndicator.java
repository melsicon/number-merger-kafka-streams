package de.melsicon.examples;

import io.micronaut.health.HealthStatus;
import io.micronaut.management.health.indicator.HealthIndicator;
import io.micronaut.management.health.indicator.HealthResult;
import io.micronaut.management.health.indicator.annotation.Liveness;
import io.micronaut.management.health.indicator.annotation.Readiness;
import io.reactivex.Flowable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.streams.KafkaStreams;
import org.reactivestreams.Publisher;

@Singleton
@Readiness
public class RedinessIndicator implements HealthIndicator {

    @Inject
    KafkaStreams kafkaStreams;

    @Override
    public Publisher<HealthResult> getResult() {
        if (kafkaStreams.state().isRunningOrRebalancing()) {
            return Flowable.just(HealthResult.builder("readiness").status(HealthStatus.UP).build());
        }
        return Flowable.just(HealthResult.builder("readiness").status(HealthStatus.DOWN).build());
    }
}
