package com.liftoff.project.mapper.impl;

import com.liftoff.project.controller.order.request.AddressRequestDTO;
import com.liftoff.project.controller.order.request.OrderItemRequestDTO;
import com.liftoff.project.model.order.Address;
import com.liftoff.project.model.order.Order;
import com.liftoff.project.model.order.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddressMapperImplTest {



    @Mock
    private AddressRequestDTO addressRequestDTO;

    @InjectMocks
    private AddressMapperImpl addressMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnNullObjectForNullAddressRequestDTO() {

        // Given
        AddressRequestDTO addressRequestDTO = null;

        // When
        Address addres= addressMapper.mapAddressRequestDTOToAddress(addressRequestDTO);

        // Then
        assertNull(addres);
    }


    @Test
    void mapAddressRequestDTOToAddress() {
        // Given
        AddressRequestDTO addressRequestDTO = AddressRequestDTO.builder()
                .street("Krzemieniowa")
                .houseNumber("56")
                .apartmentNumber("44")
                .postalCode("555-666")
                .city("LODZ")
                .country("POLSKA")
                .phoneNumber("eeeeee")
                .build();

        // When
        Address address = addressMapper.mapAddressRequestDTOToAddress(addressRequestDTO);

        // Then
        assertEquals(address.getStreet(), addressRequestDTO.getStreet());
        assertEquals(address.getHouseNumber(), addressRequestDTO.getHouseNumber());
        assertEquals(address.getApartmentNumber(), addressRequestDTO.getApartmentNumber());
        assertEquals(address.getPhoneNumber(), addressRequestDTO.getPhoneNumber());



    }
}