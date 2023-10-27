package com.liftoff.project.service.impl;

import com.liftoff.project.service.RabbitMQConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerServiceImpl implements RabbitMQConsumerService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RabbitMQConsumerServiceImpl.class);

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        LOGGER.info(String.format("Received message -> %s", message));
    }

}