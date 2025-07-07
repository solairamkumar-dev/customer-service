package com.asrk.mapper;

import com.asrk.dto.CustomerDto;
import com.asrk.dto.MovieDto;
import com.asrk.entity.Customer;

import java.util.List;

public class EntityDtoMapper {

    public static CustomerDto toDto(Customer customer, List<MovieDto> movies){
        return new CustomerDto(
                customer.getId(),
                customer.getName().toUpperCase(),
                customer.getFavoriteGenre(),
                movies
        );
    }
}
