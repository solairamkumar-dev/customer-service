package com.asrk.controller;

import com.asrk.dto.CustomerDto;
import com.asrk.dto.GenreUpdateRequest;
import com.asrk.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Integer id){
        var customerDto =  service.getCustomer(id);
        return ResponseEntity.ok(customerDto);
    }

    @PatchMapping("/{id}/genre")
    public ResponseEntity<Void> updateGenre(@PathVariable Integer id, @RequestBody GenreUpdateRequest request){
        this.service.updateCustomerGenre(id, request);
        return ResponseEntity.noContent().build();//204
    }

}
