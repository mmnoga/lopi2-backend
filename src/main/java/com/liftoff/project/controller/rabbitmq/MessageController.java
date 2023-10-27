package com.liftoff.project.controller.rabbitmq;

import com.liftoff.project.service.impl.RabbitMQProducerServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitqm")
@RequiredArgsConstructor
@Tag(name = "RabbitQM Message Controller")
public class MessageController {

    private final RabbitMQProducerServiceImpl producerService;

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {

        producerService.sendMessage(message);

        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

}