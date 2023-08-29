package com.liftoff.project.service;

import com.liftoff.project.model.Session;

import java.time.Instant;
import java.util.UUID;

public interface SessionService {

    Session createSession(UUID uId, Instant expirationTime);

}
