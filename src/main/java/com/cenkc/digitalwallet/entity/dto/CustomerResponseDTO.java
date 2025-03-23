package com.cenkc.digitalwallet.entity.dto;

import com.cenkc.digitalwallet.entity.Customer;
import lombok.Data;

@Data
public class CustomerResponseDTO {
    private String name;
    private String surname;

    public CustomerResponseDTO(Customer customer) {
        this.name = customer.getName();
        this.surname = customer.getSurname();
    }

    public CustomerResponseDTO(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public CustomerResponseDTO() {
    }

    public static CustomerResponseDTOBuilder builder() {
        return new CustomerResponseDTOBuilder();
    }

    public static class CustomerResponseDTOBuilder {
        private String name;
        private String surname;

        CustomerResponseDTOBuilder() {
        }

        public CustomerResponseDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerResponseDTOBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public CustomerResponseDTO build() {
            return new CustomerResponseDTO(name, surname);
        }

        public String toString() {
            return "CustomerResponseDTO.CustomerResponseDTOBuilder(name=" + this.name + ", surname=" + this.surname + ")";
        }
    }
}
