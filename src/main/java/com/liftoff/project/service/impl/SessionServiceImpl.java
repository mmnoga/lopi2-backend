package com.liftoff.project.service.impl;

import com.liftoff.project.model.Session;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public Session createSession(UUID uId, Instant expirationTime) {
        Session session = new Session();
        session.setUId(uId);
        session.setExpirationTime(expirationTime);
        session.setExpired(false);

        return sessionRepository.save(session);
    }

    @Override
    public void updateExpiredStatus(UUID uid) {
        Session session = sessionRepository
                .findByuId(uid)
                .orElse(null);
        if (session != null) {
            session.setExpired(true);
            sessionRepository.save(session);
        }
    }

    @Override
    public List<Session> getExpiredSessions() {
        Instant currentDate = Instant.now();

        return sessionRepository
                .findByExpirationTimeBeforeAndIsExpiredFalse(currentDate);
    }

    @Override
    public void deleteExpiredSessions() {
        List<Session> expiredSessions = sessionRepository.findByIsExpiredTrue();
        sessionRepository.deleteAll(expiredSessions);
    }

}
