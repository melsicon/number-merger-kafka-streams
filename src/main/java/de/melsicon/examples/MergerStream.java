package de.melsicon.examples;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

@Slf4j
@Factory
public class MergerStream {
    public static final String NAME = "number-merger-kafka-streams";
    public static final String CONSUMER_GROUP = "number-merger";

    @Singleton
    @Named(NAME)
    KStream<String, String> mergerStream(ConfiguredStreamBuilder streamBuilder) {
        Properties configuration = streamBuilder.getConfiguration();
        configuration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configuration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, CONSUMER_GROUP);

        KStream<String, String> stream = streamBuilder.stream("random-number-1-v1");

        stream.peek((key, value) -> log.debug("Message recieved: {}", key))
        .to("merged-topic-v1");

        return stream;
    }
}
