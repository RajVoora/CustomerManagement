package com.customerManagement.membership.repository;


import com.customerManagement.membership.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByName(String name);
    Optional<Customer> findByEmail(String email);
}

