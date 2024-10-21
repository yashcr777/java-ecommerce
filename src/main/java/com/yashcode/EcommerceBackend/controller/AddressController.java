package com.yashcode.EcommerceBackend.controller;


import com.yashcode.EcommerceBackend.entity.Address;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateAddressRequest;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.Address.IAddressService;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/addresses")
public class AddressController {
    private final IAddressService addressService;
    private final IUserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ApiResponse>createAddress(@RequestBody CreateAddressRequest request){
        try{
            User user=userService.getAuthenticatedUser();
            Address newAddress=addressService.createAddress(request,user);
            return ResponseEntity.ok(new ApiResponse("Address Added Successfully",newAddress));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


    @GetMapping("/address/{userId}/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<ApiResponse>getAddressByUserId(@RequestParam Long userId){
        try {
            User user=userService.getAuthenticatedUser();
            List<Address>addresses=addressService.getAddressByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Success",addresses));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }





    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse>deleteAddress(@PathVariable Long id)
    {
        try {
            addressService.deletedAddress(id);
            return ResponseEntity.ok(new ApiResponse("Successfully Deleted Address",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
