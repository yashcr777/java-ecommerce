package com.yashcode.EcommerceBackend.service.Address;

import com.yashcode.EcommerceBackend.Repository.AddressRepository;
import com.yashcode.EcommerceBackend.entity.Address;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateAddressRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService implements IAddressService{
    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAddressByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Address createAddress(CreateAddressRequest request, User user) {
        try {
            Address address = new Address();
            address.setCity(request.getCity());
            address.setName(request.getName());
            address.setCountry(request.getCountry());
            address.setState(request.getState());
            address.setPinCode(request.getPinCode());
            address.setUser(user);
            log.info("Address created Successfully!");
            return addressRepository.save(address);
        }
        catch(Exception e){
            log.error("Address not created");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
