package com.liftoff.project.service.impl;

import com.liftoff.project.controller.order.request.OrderSummaryDataDTO;
import com.liftoff.project.service.OrderSummaryProducerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderSummaryProducerServiceImpl implements OrderSummaryProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final String orderSummaryExchange;
    private final String orderSummaryRoutingKey;

    public OrderSummaryProducerServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${order.summary.exchange.name}") String orderSummaryExchange,
            @Value("${order.summary.routing.key}") String orderSummaryRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.orderSummaryExchange = orderSummaryExchange;
        this.orderSummaryRoutingKey = orderSummaryRoutingKey;
    }

    @Override
    public void sendOrderSummaryDataMessage(OrderSummaryDataDTO orderSummaryDataDTO) {
        rabbitTemplate
                .convertAndSend(orderSummaryExchange, orderSummaryRoutingKey, orderSummaryDataDTO);
    }

}