package com.customerManagement.membership.exceptions;


import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID id) {
        super("Customer not found with ID: " + id);
    }

    public CustomerNotFoundException(String detail) {
        super("Customer not found: " + detail);
    }
}

