package com.liftoff.project.service.impl;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.service.UserActivationProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserActivationProducerServiceImpl implements UserActivationProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public UserActivationProducerServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${user.register.exchange.name}") String exchange,
            @Value("${user.register.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendActivationUserDataMessage(ActivationUserDataDTO activationUserData) {
        rabbitTemplate
                .convertAndSend(exchange, routingKey, activationUserData);
    }

}