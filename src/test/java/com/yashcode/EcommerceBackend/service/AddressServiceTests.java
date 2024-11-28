package com.yashcode.EcommerceBackend.service;// Java
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yashcode.EcommerceBackend.Repository.AddressRepository;
import com.yashcode.EcommerceBackend.entity.Address;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;

import com.yashcode.EcommerceBackend.request.CreateAddressRequest;
import com.yashcode.EcommerceBackend.service.Address.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
@SpringBootTest
public class AddressServiceTests {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAddressByUserId() {
        Long userId = 1L;
        Address address = new Address();

        when(addressRepository.findByUserId(userId)).thenReturn(List.of(address));

        List<Address> addresses = addressService.getAddressByUserId(userId);

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        verify(addressRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testCreateAddress() {
        CreateAddressRequest request = new CreateAddressRequest(); // Replace with actual request object
        User user = new User();
        Address address = new Address();
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address createdAddress = addressService.createAddress(request, user);

        assertNotNull(createdAddress);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testCreateAddressThrowsException() {
        CreateAddressRequest request = new CreateAddressRequest(); // Replace with actual request object
        User user = new User();
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException("Error"));

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.createAddress(request, user);
        });
    }

    @Test
    public void testDeletedAddress() {
        Long addressId = 1L;
        Address address = new Address();
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.deletedAddress(addressId);

        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    public void testDeletedAddressNotFound() {
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            addressService.deletedAddress(addressId);
        });
    }
}
