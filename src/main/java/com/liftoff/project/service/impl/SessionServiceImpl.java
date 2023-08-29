package com.liftoff.project.service.impl;

import com.liftoff.project.model.Session;
import com.liftoff.project.repository.SessionRepository;
import com.liftoff.project.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

}
