package com.customerManagement.membership.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    private BigDecimal annualSpend;

    private LocalDate lastPurchaseDate;
    
    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tier;
    
    public void setTierBasedOnRules() {
        if (annualSpend == null || lastPurchaseDate == null) {
            this.tier = null;
            return;
        }

        LocalDate today = LocalDate.now();
        Period sinceLastPurchase = Period.between(lastPurchaseDate, today);

        if (annualSpend.compareTo(BigDecimal.valueOf(10000)) >= 0 && sinceLastPurchase.toTotalMonths() <= 6) {
            this.tier = "Platinum";
        } else if (annualSpend.compareTo(BigDecimal.valueOf(1000)) >= 0 &&
                   annualSpend.compareTo(BigDecimal.valueOf(10000)) < 0 &&
                   sinceLastPurchase.toTotalMonths() <= 12) {
            this.tier = "Gold";
        } else if (annualSpend.compareTo(BigDecimal.valueOf(1000)) < 0) {
            this.tier = "Silver";
        } else {
            this.tier = null;
        }
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}

