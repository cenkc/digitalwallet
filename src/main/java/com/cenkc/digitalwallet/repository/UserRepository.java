package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
}
