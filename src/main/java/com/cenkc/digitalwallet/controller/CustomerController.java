package com.cenkc.digitalwallet.controller;

//@RestController
//@RequestMapping("/api/v1/customers")
public class CustomerController {
/*

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseDTO> addCustomer(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        return new ResponseEntity<>(customerService.addCustomer(customerRequestDTO), HttpStatus.CREATED);
    }

    // Find all customers whether deleted or not
    // TODO can only seen by ADMIN
    @GetMapping(value = {"", "/"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> findAll(){
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    // Delete customer by id
    // TODO can only deleted by ADMIN
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable(name="id") long id){
        boolean deleted = customerService.delete(id);
        return new ResponseEntity<>(true, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Find all active customers
    @GetMapping(value = "/active")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> findAllActive(){
        return new ResponseEntity<>(customerService.findAllActive(), HttpStatus.OK);
    }

    // Find all deleted customers
    // TODO can only seen by ADMIN
    @GetMapping(value = "/deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> findAllDeleted(){
        return new ResponseEntity<>(customerService.findAllDeleted(), HttpStatus.OK);
    }
*/

}
