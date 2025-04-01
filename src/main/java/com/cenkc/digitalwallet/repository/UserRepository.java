package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.User;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    User findByTckn(String tckn);

    //    @Query("SELECT u FROM User u WHERE u.deleted = false")
//    List<User> findAllActive();
}
