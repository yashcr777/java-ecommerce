package com.yashcode.EcommerceBackend.controller;


import com.yashcode.EcommerceBackend.entity.Address;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateAddressRequest;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.Address.IAddressService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
 class AddressControllerTests {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private IAddressService addressService;

    @Mock
    private IUserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        addressController=new AddressController(addressService,userService);
    }


    @Test
    void testCreateAddress_Success() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        User user = new User();
        Address address = new Address();

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressService.createAddress(request, user)).thenReturn(address);

        ResponseEntity<ApiResponse> response = addressController.createAddress(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Address Added Successfully", response.getBody().getMessage());
        assertEquals(address, response.getBody().getData());
    }


    @Test
    void testCreateAddress_ResourceNotFound() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();

        when(userService.getAuthenticatedUser()).thenThrow(new ResourceNotFoundException("User not found"));

        ResponseEntity<ApiResponse> response = addressController.createAddress(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }


    @Test
    void testGetAddressByUserId_Success() throws Exception {
        Long userId = 1L;
        User user = new User();
        Address address1 = new Address();
        Address address2 = new Address();
        List<Address> addresses = Arrays.asList(address1, address2);

        when(userService.getAuthenticatedUser()).thenReturn(user);
        when(addressService.getAddressByUserId(userId)).thenReturn(addresses);

        ResponseEntity<ApiResponse> response = addressController.getAddressByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(addresses, response.getBody().getData());
    }


    @Test
    void testGetAddressByUserId_ResourceNotFound() throws Exception {
        Long userId = 1L;

        when(userService.getAuthenticatedUser()).thenThrow(new ResourceNotFoundException("User not found"));

        ResponseEntity<ApiResponse> response = addressController.getAddressByUserId(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }


    @Test
    void testDeleteAddress_Success() throws Exception {
        Long addressId = 1L;

        doNothing().when(addressService).deletedAddress(addressId);

        ResponseEntity<ApiResponse> response = addressController.deleteAddress(addressId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Successfully Deleted Address", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }


    @Test
    void testDeleteAddress_ResourceNotFound() throws Exception {
        Long addressId = 1L;

        doThrow(new ResourceNotFoundException("Address not found")).when(addressService).deletedAddress(addressId);

        ResponseEntity<ApiResponse> response = addressController.deleteAddress(addressId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Address not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
 