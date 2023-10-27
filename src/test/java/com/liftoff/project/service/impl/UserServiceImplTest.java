package com.liftoff.project.service.impl;

import com.liftoff.project.controller.auth.request.ActivationUserDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceImplTest {

    @InjectMocks
    private UserActivationProducerServiceImpl userActivationProducerService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Value("${user.register.exchange.name}")
    private String exchange;

    @Value("${user.register.routing.key}")
    private String routingKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldSendActivationUserDataToRabbitMQ() {
        // given
        ActivationUserDataDTO activationUserData = ActivationUserDataDTO.builder()
                .encodedUsername("sampleEncodedUsername")
                .tokenValue("sampleTokenValue")
                .build();

        // when
        userActivationProducerService.sendActivationUserDataMessage(activationUserData);

        // then
        Mockito.verify(rabbitTemplate, Mockito.times(1))
                .convertAndSend(exchange, routingKey, activationUserData);
    }

}