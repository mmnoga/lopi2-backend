package com.liftoff.project.repository;

import com.liftoff.project.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByExpirationTimeBeforeAndIsExpiredFalse(Instant currentDate);

    Optional<Session> findByuId(UUID uId);

}
