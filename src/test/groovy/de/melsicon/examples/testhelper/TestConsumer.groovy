package de.melsicon.examples.testhelper

import de.melsicon.examples.MergerStream
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic

import static de.melsicon.examples.StreamTest.received
import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

@KafkaListener(offsetReset = EARLIEST)
class TestConsumer {

    @Topic(MergerStream.OUTPUT_TOPIC)
    void updateAnalytics(String sum) {
        received.add(sum)
    }
}
