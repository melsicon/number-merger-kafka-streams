package de.melsicon.examples;

import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueJoiner;

import java.util.Properties;

import static de.melsicon.examples.CustomValueJoiner.createJoiner;

@Slf4j
@Factory
public class MergerStream {
    public static final String NAME = "number-merger-kafka-streams";
    public static final String CONSUMER_GROUP = "number-merger";
    public static final String INPUT_TOPIC_1 = "random-number-1-v1";
    public static final String INPUT_TOPIC_2 = "random-number-2-v1";
    public static final String OUTPUT_TOPIC = "merged-topic-v1";

    @Singleton
    @Named(NAME)
    KStream<String, String> mergerStream(ConfiguredStreamBuilder streamBuilder) {
        Properties configuration = streamBuilder.getConfiguration();
        configuration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configuration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configuration.put(StreamsConfig.APPLICATION_ID_CONFIG, CONSUMER_GROUP);

        KStream<String, String> source1 = streamBuilder.stream(INPUT_TOPIC_1);
        KStream<String, String> source2 = streamBuilder.stream(INPUT_TOPIC_2);

        source1.peek((k, v) -> log.info("Message received source1: {} {}", k,v));
        source2.peek((k, v) -> log.info("Message received source2: {} {}", k,v));

        ValueJoiner<String, String, String> valueJoiner = createJoiner();
        KStream<String, String> result = source1.join(source2, valueJoiner, JoinWindows.of(5000L));  //1.8mn ms = 30min
        result.to(OUTPUT_TOPIC);
        return source1;
    }
}
