package de.melsicon.examples

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Ignore
import spock.lang.Specification

@MicronautTest
@Ignore  //Kann nur laufen wenn auch ein Kafka-Broker läuft
class RandomNumberGenSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'test it works'() {
        expect:
        application.running
    }

}
