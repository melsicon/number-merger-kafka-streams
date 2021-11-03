# Random Number Generator

This random Number Generator generates a random number and sends it to a kafka topic with key: <number of created messages> and value: <random number as string>

## Setup on MacOS

install homebrew: https://brew.sh/

Install Java, Docker, IntelliJ, k9s, ...

    brew install openjdk k9s kafka gradle kubernetes-cli micronaut
    brew install --cask intellij-idea-ce docker
    
Get a docker account and setup the "number-merger-kafka-streams" repo

Configure docker to run kubernetes (k8s) -> See Settings of Docker

start zookeeper

    zookeeper-server-start kafka-config/zookeeper.properties &

start kafka
    
    kafka-server-start kafka-config/server.properties &


Change the docker image path in your build.gradle and k8s.yml. 
Login to your Docker Desktop application.

    ./gradlew clean build jib

    kubectl create namespace dev

    kubectl apply -f k8s.yml -n dev
    
See log form pod

    kubectl get pods -n dev
    
    kubectl logs number-merger-kafka-streams-<something-something> -n dev -f

Read from Kafka

    kafka-console-consumer --bootstrap-server localhost:9092 --topic random-number-1-v1 --property key=value --property print.key=true --property key.separator="-" --from-beginning

Stop Kafka

    zookeeper-server-stop && kafka-server-stop
    
    
## Links 

https://docs.micronaut.io/latest/guide/

https://micronaut-projects.github.io/micronaut-kafka/latest/guide/


## Micronaut 3.0.1 Documentation

- [User Guide](https://docs.micronaut.io/3.0.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.0.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.0.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)

## Feature kubernetes documentation

- [Micronaut Kubernetes Support documentation](https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html)

- [https://kubernetes.io/docs/home/](https://kubernetes.io/docs/home/)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Feature management documentation

- [Micronaut Management documentation](https://docs.micronaut.io/latest/guide/index.html#management)

## Feature lombok documentation

- [Micronaut Project Lombok documentation](https://docs.micronaut.io/latest/guide/index.html#lombok)

- [https://projectlombok.org/features/all](https://projectlombok.org/features/all)

