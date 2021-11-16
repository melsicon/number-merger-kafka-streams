package de.melsicon.examples.testhelper

import io.micronaut.test.support.TestPropertyProvider
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

class KafkaTestContainer extends Specification implements TestPropertyProvider {

    static final KafkaContainer kafkaContainer

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
        kafkaContainer.start()
        def bootstrapServers = kafkaContainer.getBootstrapServers()
        createTopics(bootstrapServers)

    }

    static def createTopics(String bootstrapServers) {
        def adminClient = AdminClient.create(["bootstrap.servers": bootstrapServers])
        adminClient.createTopics(["random-number-1-v1", "random-number-2-v1", "merged-topic-v1"]
                .collect { topic -> new NewTopic(topic, 1, 1 as short) })
    }

    @Override
    Map<String, String> getProperties() {
        return Collections.singletonMap("kafka.bootstrap.servers", kafkaContainer.getBootstrapServers())
    }
}
