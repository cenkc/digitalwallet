package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.Customer;
import com.cenkc.digitalwallet.entity.dto.CustomerRequestDTO;
import com.cenkc.digitalwallet.entity.dto.CustomerResponseDTO;
import com.cenkc.digitalwallet.exception.BadRequestException;
import com.cenkc.digitalwallet.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get all customers (including deleted ones)
    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Get all active (non-deleted) customers via filter
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAllActive() {
        Session session = entityManager.unwrap(Session.class);
        //session.enableFilter("deletedCustomerFilter").setParameter("deleted", false);
        Filter deletedCustomerFilter = session.enableFilter("deletedCustomerFilter");
        deletedCustomerFilter.setParameter("isDeleted", false);
        List<Customer> customers = customerRepository.findAll();
        session.disableFilter("deletedCustomerFilter");
        return customers.stream()
                .map(CustomerResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Get all deleted customers via filter
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> findAllDeleted() {
        Session session = entityManager.unwrap(Session.class);
        //session.enableFilter("deletedCustomerFilter").setParameter("deleted", true);
        Filter deletedCustomerFilter = session.enableFilter("deletedCustomerFilter");
        deletedCustomerFilter.setParameter("isDeleted", true);
        List<Customer> customers = customerRepository.findAll();
        session.disableFilter("deletedCustomerFilter");

        return customers.stream()
                .map(CustomerResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Soft delete (uses @SQLDelete)
    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new BadRequestException("Customer not found");
        }
        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public CustomerResponseDTO addCustomer(CustomerRequestDTO customerRequestDTO) {
        // convert customerRequestDTO to Customer entity
        Customer customer = Customer.builder()
                .name(customerRequestDTO.getName())
                .surname(customerRequestDTO.getSurname())
                .tckn(customerRequestDTO.getTckn())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerResponseDTO.builder()
                .name(savedCustomer.getName())
                .surname(savedCustomer.getSurname())
                .build();
    }
}
