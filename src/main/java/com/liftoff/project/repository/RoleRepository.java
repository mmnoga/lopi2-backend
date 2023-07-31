package com.liftoff.project.repository;


import com.liftoff.project.model.Role;
import com.liftoff.project.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {



    Role findByRoleName(RoleName roleName);

    Optional<Role> findByUuid(UUID uuid);
}
