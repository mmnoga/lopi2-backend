package com.liftoff.project.service;

public interface RabbitMQConsumerService {

    /**
     * Listens for and consumes messages from a RabbitMQ queue.
     *
     * @param message The received message content from the queue.
     */
    void consume(String message);

}