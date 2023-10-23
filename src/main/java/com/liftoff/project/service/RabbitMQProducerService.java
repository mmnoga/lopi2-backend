package com.liftoff.project.service;

public interface RabbitMQProducerService {

    /**
     * Sends a message to RabbitMQ using the provided exchange, routing key, and message content.
     *
     * @param message The message content to be sent to RabbitMQ.
     */
    void sendMessage(String message);

}