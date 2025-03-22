package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponseDTO {
    private String name;
    private String surname;

    public CustomerResponseDTO(Customer customer) {
        this.name = customer.getName();
        this.surname = customer.getSurname();
    }
}
