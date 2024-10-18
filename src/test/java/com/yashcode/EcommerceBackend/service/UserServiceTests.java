package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {


    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllUser(){
        assertNotNull(userRepository.findAll());
    }
}