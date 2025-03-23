package com.cenkc.digitalwallet.controller;

import com.cenkc.digitalwallet.entity.dto.CustomerRequestDTO;
import com.cenkc.digitalwallet.entity.dto.CustomerResponseDTO;
import com.cenkc.digitalwallet.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping
    public ResponseEntity<CustomerResponseDTO> addCustomer(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        return new ResponseEntity<>(customerService.addCustomer(customerRequestDTO), HttpStatus.CREATED);
    }

    // Find all customers whether deleted or not
    // TODO can only seen by ADMIN
    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<CustomerResponseDTO>> findAll(){
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    // Delete customer by id
    // TODO can only deleted by ADMIN
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable(name="id") long id){
        boolean deleted = customerService.delete(id);
        return new ResponseEntity<>(true, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Find all active customers
    @GetMapping(value = "/active")
    public ResponseEntity<List<CustomerResponseDTO>> findAllActive(){
        return new ResponseEntity<>(customerService.findAllActive(), HttpStatus.OK);
    }

    // Find all deleted customers
    // TODO can only seen by ADMIN
    @GetMapping(value = "/deleted")
    public ResponseEntity<List<CustomerResponseDTO>> findAllDeleted(){
        return new ResponseEntity<>(customerService.findAllDeleted(), HttpStatus.OK);
    }

}
