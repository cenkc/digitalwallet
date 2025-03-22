package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.Customer;
import com.cenkc.digitalwallet.entity.dto.CustomerRequestDTO;
import com.cenkc.digitalwallet.entity.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO);
    public boolean delete(Long id);
    public List<CustomerResponseDTO> findAllDeleted();
    public List<CustomerResponseDTO> findAllActive();
    public List<CustomerResponseDTO> findAll();
}
