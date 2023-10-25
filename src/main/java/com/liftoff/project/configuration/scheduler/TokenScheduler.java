package com.liftoff.project.configuration.scheduler;

import com.liftoff.project.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final TokenService tokenService;

    @Scheduled(cron = "${token.register.deleteExpiredOccurrence}")
    public void deleteExpired() {
        tokenService.deleteAllExpired();
    }

}