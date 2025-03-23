package com.cenkc.digitalwallet.repository;

import com.cenkc.digitalwallet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @Query("SELECT c FROM Customer c WHERE c.deleted = false")
//    List<Customer> findAllActive();
}
