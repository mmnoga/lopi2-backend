package com.liftoff.project.repository;

import com.liftoff.project.model.order.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
