package com.liftoff.project.service.impl;

import com.liftoff.project.service.RabbitMQProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerServiceImpl implements RabbitMQProducerService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RabbitMQProducerServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    @Autowired
    public RabbitMQProducerServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name}") String exchange,
            @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        LOGGER.info(String.format("Message sent -> %s", message));
    }

}