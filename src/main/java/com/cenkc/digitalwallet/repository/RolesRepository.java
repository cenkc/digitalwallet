package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
}
