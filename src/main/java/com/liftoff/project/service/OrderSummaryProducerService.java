package com.liftoff.project.service;

import com.liftoff.project.controller.order.request.OrderSummaryDataDTO;

public interface OrderSummaryProducerService {

    /**
     * Sends a message containing order summary data to a message queue.
     *
     * @param orderSummaryDataDTO The OrderSummaryDataDTO object containing the order summary data to be sent.
     */
    void sendOrderSummaryDataMessage(OrderSummaryDataDTO orderSummaryDataDTO);

}