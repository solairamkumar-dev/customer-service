package com.asrk.service;

import com.asrk.client.MovieClient;
import com.asrk.dto.CustomerDto;
import com.asrk.dto.GenreUpdateRequest;
import com.asrk.exceptions.CustomerNotFoundException;
import com.asrk.mapper.EntityDtoMapper;
import com.asrk.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final MovieClient movieClient;
    private final CustomerRepository repository;

    public CustomerService(MovieClient movieClient, CustomerRepository repository){
        this.movieClient = movieClient;
        this.repository = repository;
    }

    public CustomerDto getCustomer(Integer id){
        var customer = this.repository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException(id));
        var movies = this.movieClient.getMovies(customer.getFavoriteGenre());
        return EntityDtoMapper.toDto(customer, movies);
    }

    public void updateCustomerGenre(Integer id, GenreUpdateRequest request){
        var customer = this.repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setFavoriteGenre(request.favoriteGenre());
        this.repository.save(customer);
    }

}
