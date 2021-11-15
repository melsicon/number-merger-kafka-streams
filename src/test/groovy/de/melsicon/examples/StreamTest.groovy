package de.melsicon.examples

import de.melsicon.examples.testhelper.TestConsumer
import de.melsicon.examples.testhelper.TestGenerator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

import java.util.concurrent.ConcurrentLinkedDeque

import static de.melsicon.examples.testhelper.TopicCreator.createTopics
import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await

@Testcontainers
@MicronautTest
class StreamTest extends Specification implements TestPropertyProvider {

    public static final Collection<String> received = new ConcurrentLinkedDeque<>()
    //static kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))

    static KafkaContainer kafkaContainer
    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
        kafkaContainer.start()
        def bootstrapServers = kafkaContainer.getBootstrapServers()

        createTopics(bootstrapServers)

    }

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


    @Override
    Map<String, String> getProperties() {
        return Collections.singletonMap("kafka.bootstrap.servers", kafkaContainer.getBootstrapServers())
    }
}
