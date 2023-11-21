package com.liftoff.project.configuration.payu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "payu")
public class PayUConfig {

    private String clientId;
    private String clientSecret;
    private String merchantPosId;

}