package com.liftoff.project.configuration.scheduler;

import com.liftoff.project.service.SessionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class SessionScheduler {

    private final SessionService sessionService;

    @Scheduled(cron = "${cart.cookie.sessionRemoverOccurrence}")
    public void updateExpiredStatus() {
        log.info("Update expired session status...");
        sessionService.getExpiredSessions()
                .forEach(session -> sessionService
                        .updateExpiredStatus(session.getUId()));
    }

}