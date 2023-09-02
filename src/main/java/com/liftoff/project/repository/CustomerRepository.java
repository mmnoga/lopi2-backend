package com.liftoff.project.repository;

import com.liftoff.project.model.order.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
