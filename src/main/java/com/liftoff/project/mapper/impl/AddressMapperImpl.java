package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.mapper.AddressMapper;
import com.liftoff.project.mapper.ProductMapper;
import com.liftoff.project.model.order.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapperImpl implements AddressMapper {


    private ProductMapper productMapper;

    @Override
    public Address mapAddressRequestDTOToAddress(AddressRequestDTO addressRequestDTO) {


        Address address = Address.builder()
                .street(addressRequestDTO.getStreet())
                .houseNumber(addressRequestDTO.getHouseNumber())
                .apartmentNumber(addressRequestDTO.getApartmentNumber())
                .postalCode(addressRequestDTO.getPostalCode())
                .city(addressRequestDTO.getCity())
                .country(addressRequestDTO.getCountry())
                .build();

        return address;
    }


}