package com.liftoff.project.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQResetUserPasswordConfig {

    private final String queue;
    private final String exchange;
    private final String routingKey;

    @Autowired
    public RabbitMQResetUserPasswordConfig(@Value("${user.passwordReset.queue.name}") String queue,
                                      @Value("${user.passwordReset.exchange.name}") String exchange,
                                      @Value("${user.passwordReset.routing.key}") String routingKey) {
        this.queue = queue;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Bean
    public Queue resetUserPasswordQueue() {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange resetUserPasswordExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding resetUserPasswordBinding() {
        return BindingBuilder.bind(resetUserPasswordQueue())
                .to(resetUserPasswordExchange())
                .with(routingKey);
    }

}