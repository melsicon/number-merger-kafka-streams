package de.melsicon.examples.testhelper

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic

class TopicCreator {

    static def createTopics(String bootstrapServers) {
        def adminClient = AdminClient
                .create(["bootstrap.servers": bootstrapServers])
        adminClient
                .createTopics(["random-number-1-v1", "random-number-2-v1", "merged-topic-v1"]
                        .collect { topic -> new NewTopic(topic, 1, 1 as short) })
    }
}
