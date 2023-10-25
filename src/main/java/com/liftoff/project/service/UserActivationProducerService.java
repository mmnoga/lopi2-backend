package com.liftoff.project.service;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;

public interface UserActivationProducerService {

    /**
     * Sends an activation user data message to a RabbitMQ exchange.
     *
     * This method sends the provided activationUserData object as a message to the specified RabbitMQ exchange
     * using the configured RabbitTemplate.
     *
     * @param activationUserData The ActivationUserDataDTO object containing user activation data to send.
     */
    void sendActivationUserDataMessage(ActivationUserDataDTO activationUserData);

}