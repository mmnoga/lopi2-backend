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

    /**
     * Retrieves a list of sessions that have expiration time before the given current date
     * and are not marked as expired.
     *
     * @param currentDate The current date to compare with session expiration times.
     * @return A list of sessions that meet the criteria.
     */
    List<Session> findByExpirationTimeBeforeAndIsExpiredFalse(Instant currentDate);

    /**
     * Retrieves an optional session by its unique identifier (UUID).
     *
     * @param uId The unique identifier (UUID) of the session to retrieve.
     * @return An Optional containing the session with the specified UUID, or empty if not found.
     */
    Optional<Session> findByuId(UUID uId);

    /**
     * Retrieves a list of sessions that are marked as expired.
     *
     * @return A list of sessions that are marked as expired.
     */
    List<Session> findByIsExpiredTrue();

}
