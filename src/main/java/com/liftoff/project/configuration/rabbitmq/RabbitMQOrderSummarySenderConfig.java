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
public class RabbitMQOrderSummarySenderConfig {

    private final String queue;
    private final String exchange;
    private final String routingKey;

    @Autowired
    public RabbitMQOrderSummarySenderConfig(@Value("${order.summary.queue.name}") String queue,
                                           @Value("${order.summary.exchange.name}") String exchange,
                                           @Value("${order.summary.routing.key}") String routingKey) {
        this.queue = queue;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Bean
    public Queue orderSummaryQueue() {
        return new Queue(queue);
    }

    @Bean
    public TopicExchange orderSummaryExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding orderSummaryBinding() {
        return BindingBuilder.bind(orderSummaryQueue())
                .to(orderSummaryExchange())
                .with(routingKey);
    }

}