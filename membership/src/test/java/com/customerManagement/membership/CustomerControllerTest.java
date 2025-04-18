package com.customerManagement.membership;


import com.customerManagement.membership.model.Customer;
import com.customerManagement.membership.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    private Customer customer;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        customer = new Customer();
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setAnnualSpend(new BigDecimal("5000.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(6));
        repository.save(customer);
    }

    @Test
    public void testCreateCustomer() throws Exception {
        String json = """
            {
              "name": "Alice",
              "email": "alice@example.com",
              "annualSpend": 12000,
              "lastPurchaseDate": "2024-12-01"
            }
        """;

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test User")));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        String json = """
            {
              "name": "Updated Name",
              "email": "updated@example.com",
              "annualSpend": 9500,
              "lastPurchaseDate": "2024-11-01"
            }
        """;

        mockMvc.perform(put("/customers/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/customers/" + customer.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testTierSilver() throws Exception {
        customer.setAnnualSpend(new BigDecimal("500.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(24));
        repository.save(customer);

        mockMvc.perform(get("/customers/" + customer.getId()))
                .andExpect(jsonPath("$.tier", is("Silver")));
    }

    @Test
    public void testTierGold() throws Exception {
        customer.setAnnualSpend(new BigDecimal("5000.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(6));
        repository.save(customer);

        mockMvc.perform(get("/customers/" + customer.getId()))
                .andExpect(jsonPath("$.tier", is("Gold")));
    }

    @Test
    public void testTierPlatinum() throws Exception {
        customer.setAnnualSpend(new BigDecimal("15000.00"));
        customer.setLastPurchaseDate(LocalDate.now().minusMonths(3));
        repository.save(customer);

        mockMvc.perform(get("/customers/" + customer.getId()))
                .andExpect(jsonPath("$.tier", is("Platinum")));
    }

    @Test
    public void testInvalidEmail() throws Exception {
        String json = """
            {
              "name": "Bob",
              "email": "invalid-email",
              "annualSpend": 1000,
              "lastPurchaseDate": "2024-12-01"
            }
        """;

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}

