package de.melsicon.examples;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomValueJoiner {

    static org.apache.kafka.streams.kstream.ValueJoiner<String, String, String> createJoiner() {
        return (value1, value2) -> {
            try {
                Double sum = Double.parseDouble(value1) + Double.parseDouble(value2);
                log.info(value1 + " + " + value2 + " = "+ sum);
                return sum.toString(); //String.valueOf(sum);
            } catch (NumberFormatException e) {
                log.warn("Parsing Fehler im CustomValueJoiner: " + e.getMessage());
                return "999.9"; //"Something went wrong Parsing.";
            }
        };
    }
}
