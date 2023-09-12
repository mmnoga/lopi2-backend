package com.liftoff.project.repository;

import com.liftoff.project.model.order.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {

    Optional<DeliveryMethod> findByName(String name);

}
