package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTests {


    @Autowired
    private UserRepository userRepository;

    @Test
    public void testgetAllUser(){
        assertNotNull(userRepository.findAll());
    }
}