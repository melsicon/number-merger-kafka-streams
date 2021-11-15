package de.melsicon.examples.testhelper

import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Slf4j
@Singleton
class TestGenerator {

    @Inject
    KafkaTestTopicsProducer producer


    //@EventListener
    void generateNumbers(){
        def samplesProducerOne = [
                '1': '2',
                '2': '7',
                '3': '8',
        ]

        def samplesProducerTwo = [
                '1': '1',
                '2': '5',
                '3': '4',
        ]

        samplesProducerOne.each { key, val ->
            producer.sendProductProducerOne("" + key, "" + val)
            producer.sendProductProducerTwo("" + key, "" + samplesProducerTwo.get(key))
            log.info("Producer 1 sent key,value -> {}, {}", key, val)
            log.info("Producer 2 sent key,value -> {}, {}", key, samplesProducerTwo.get(key))
        }


    }
}