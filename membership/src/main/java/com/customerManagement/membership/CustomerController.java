package com.customerManagement.membership;



import com.customerManagement.membership.exceptions.CustomerNotFoundException;
import com.customerManagement.membership.model.Customer;
import com.customerManagement.membership.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;


import java.util.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    // Create
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        customer.setId(null); // Ensure ID is not set manually
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(customer));
    }

    // Get by ID
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable  UUID id) {
        Customer customer = repository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setTierBasedOnRules();
        return customer;
    }

    // Get by name
    @GetMapping("/by-name")   
    public Customer getCustomerByName(
    		@RequestParam String name) {
    	logger.info("customer name: "+name);
    	Customer customer = repository.findByName(name)         
                .orElseThrow(() -> new CustomerNotFoundException(name));
    	customer.setTierBasedOnRules();
    	return customer;
    }

    // Get by email
    @GetMapping("/by-email")
    public Customer getCustomerByEmail(@RequestParam String email) {
    	logger.info("customer email: "+email);
    	Customer customer =  repository.findByEmail(email)
    			.orElseThrow(() -> new CustomerNotFoundException(email));
        customer.setTierBasedOnRules();
        return customer;
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @Valid @RequestBody Customer updated) {
    	logger.info("customer id: "+id);
        return repository.findById(id).map(customer -> {
            customer.setName(updated.getName());
            customer.setEmail(updated.getEmail());
            customer.setAnnualSpend(updated.getAnnualSpend());
            customer.setLastPurchaseDate(updated.getLastPurchaseDate());
            return ResponseEntity.ok(repository.save(customer));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
    	logger.info("customer id: "+id);
        return repository.findById(id).map(customer -> {
            repository.delete(customer);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

