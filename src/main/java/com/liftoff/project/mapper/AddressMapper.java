package com.liftoff.project.mapper;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.model.order.Address;

public interface AddressMapper {

    Address mapAddressRequestDTOToAddress(AddressRequestDTO addressRequestDTO);


}
