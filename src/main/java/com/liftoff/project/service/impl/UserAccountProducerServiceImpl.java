package com.liftoff.project.service.impl;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.ResetPasswordDataDTO;
import com.liftoff.project.service.UserAccountProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserAccountProducerServiceImpl implements UserAccountProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final String activationExchange;
    private final String activationRoutingKey;
    private final String resetExchange;
    private final String resetRoutingKey;

    public UserAccountProducerServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${user.register.exchange.name}") String activationExchange,
            @Value("${user.register.routing.key}") String activationRoutingKey,
            @Value("${user.passwordReset.exchange.name}") String resetExchange,
            @Value("${user.passwordReset.routing.key}") String resetRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.activationExchange = activationExchange;
        this.activationRoutingKey = activationRoutingKey;
        this.resetExchange = resetExchange;
        this.resetRoutingKey = resetRoutingKey;
    }

    @Override
    public void sendActivationUserDataMessage(ActivationUserDataDTO activationUserData) {
        rabbitTemplate
                .convertAndSend(activationExchange, activationRoutingKey, activationUserData);
    }

    @Override
    public void sendResetPasswordDataMessage(ResetPasswordDataDTO resetPasswordData) {
        rabbitTemplate
                .convertAndSend(resetExchange, resetRoutingKey, resetPasswordData);
    }

}