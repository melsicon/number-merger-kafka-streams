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

    private ValueJoiner<String, String, String> createJoiner() {
        return (value1, value2) -> {
            try {
                Double sum = Double.parseDouble(value1) + Double.parseDouble(value2);
                log.info(value1 + " + " + value2 + " = "+ sum);
                return sum.toString(); //String.valueOf(sum);
            } catch (NumberFormatException e) {
                log.warn("Parsing Fehler im ValueJoiner: " + e.getMessage());
                return "999.9"; //"Something went wrong Parsing.";
            }
        };
    }


    private ValueJoiner<String, String, Double> createJoinedObject() {
        return (value1, value2) -> {
            try {
                Double sum = Double.parseDouble(value1) + Double.parseDouble(value2);
                log.info(value1 + " + " + value2 + " = "+ sum);
                return sum; //String.valueOf(sum);
            } catch (NumberFormatException e) {
                log.warn("Parsing Fehler im ValueJoiner: " + e.getMessage());
                return 999.9; //"Something went wrong Parsing.";
            }
        };
    }
}
