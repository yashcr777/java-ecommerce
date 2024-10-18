package com.yashcode.EcommerceBackend.service.Address;

import com.yashcode.EcommerceBackend.entity.Address;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.request.CreateAddressRequest;

import java.util.List;

public interface IAddressService {
    List<Address> getAddressByUserId(Long userId);
    Address createAddress(CreateAddressRequest request, User user);
    void deletedAddress(Long id);
}
