package com.cafeteria.app.repository;

import com.cafeteria.app.model.Role;
import com.cafeteria.app.model.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
