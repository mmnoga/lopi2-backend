package com.liftoff.project.configuration;

import com.liftoff.project.model.order.PaymentMethod;
import com.liftoff.project.repository.PaymentMethodRepository;
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
public class PaymentMethodInitializer implements CommandLineRunner {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void run(String... args) {
        initPaymentMethods();
    }

    private void initPaymentMethods() {
        log.info("Payment methods setting up...");

        PaymentMethod blik = PaymentMethod.builder()
                .name("BLIK")
                .description("Blik payment method")
                .build();

        PaymentMethod bankTransfer = PaymentMethod.builder()
                .name("BANK_TRANSFER")
                .description("Bank transfer payment method")
                .build();

        PaymentMethod creditCard = PaymentMethod.builder()
                .name("CREDIT_CARD")
                .description("Credit card payment method")
                .build();

        PaymentMethod cod = PaymentMethod.builder()
                .name("COD")
                .description("Cash on delivery payment method")
                .build();

        PaymentMethod gateway = PaymentMethod.builder()
                .name("GATEWAY")
                .description("Gateway payment method")
                .build();

        paymentMethodRepository.saveAll(List.of(blik, bankTransfer, creditCard, cod, gateway));
    }

}
