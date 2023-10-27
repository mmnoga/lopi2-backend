package com.liftoff.project.service;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import com.liftoff.project.controller.auth.request.ResetPasswordDataDTO;

public interface UserAccountProducerService {

    /**
     * Sends an activation user data message to a RabbitMQ exchange.
     *
     * This method sends the provided activationUserData object as a message to the specified RabbitMQ exchange
     * using the configured RabbitTemplate.
     *
     * @param activationUserData The ActivationUserDataDTO object containing user activation data to send.
     */
    void sendActivationUserDataMessage(ActivationUserDataDTO activationUserData);

    /**
     * Sends a reset user password data message to a RabbitMQ exchange.
     *
     * This method sends the provided resetPasswordData object as a message to the specified RabbitMQ exchange
     * using the configured RabbitTemplate.
     *
     * @param resetPasswordData The ResetPasswordDataDTO object containing reset user password data to send.
     */
    void sendResetPasswordDataMessage(ResetPasswordDataDTO resetPasswordData);

}