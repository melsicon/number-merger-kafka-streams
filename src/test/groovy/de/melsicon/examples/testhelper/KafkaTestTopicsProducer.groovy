package de.melsicon.examples.testhelper

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface KafkaTestTopicsProducer {

    @Topic("random-number-1-v1")
    void sendProductProducerOne(@KafkaKey String key, String value);

    @Topic("random-number-2-v1")
    void sendProductProducerTwo(@KafkaKey String key, String value);




}
