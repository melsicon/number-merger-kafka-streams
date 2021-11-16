package de.melsicon.examples

import de.melsicon.examples.testhelper.KafkaTestContainer
import de.melsicon.examples.testhelper.TestGenerator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.testcontainers.spock.Testcontainers

import java.util.concurrent.ConcurrentLinkedDeque

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

@Testcontainers
@MicronautTest
class StreamTest extends KafkaTestContainer {

    public static final Collection<String> received = new ConcurrentLinkedDeque<>()
    @Inject TestGenerator testGenerator

    def "testIfStreamMergesNumbers"() {
        given:


        when:
        testGenerator.generateNumbers()

        then:
        await().atMost(30, SECONDS).until(() -> !received.isEmpty());
        received.poll()=="3.0"
        received.poll()=="12.0"
        received.poll()=="12.0"
    }
}
