package com.liftoff.project.configuration;

import com.liftoff.project.model.order.DeliveryMethod;
import com.liftoff.project.repository.DeliveryMethodRepository;
import jakarta.annotation.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Priority(1)
@Log4j2
public class DeliveryMethodInitializer implements CommandLineRunner {

    private final DeliveryMethodRepository deliveryMethodRepository;

    @Override
    public void run(String... args) {

        log.info("Delivery methods setting up...");

        DeliveryMethod inPost = DeliveryMethod.builder()
                .name("IN_POST")
                .description("InPost delivery method")
                .cost(10.00)
                .build();

        DeliveryMethod courier = DeliveryMethod.builder()
                .name("COURIER_SERVICE")
                .description("Courier service delivery method")
                .cost(20.00)
                .build();

        DeliveryMethod inStore = DeliveryMethod.builder()
                .name("IN_STORE_PICKUP")
                .description("In store pickup delivery method")
                .cost(0.00)
                .build();

        deliveryMethodRepository.saveAll(List.of(inPost, courier, inStore));
    }

}