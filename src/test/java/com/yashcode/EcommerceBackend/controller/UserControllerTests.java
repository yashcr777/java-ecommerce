package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.dto.UserDto;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.ForgotPasswordRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private IUserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        User user = new User();
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(userService.convertUserToDto(any(User.class))).thenReturn(new UserDto());

        // Act
        ResponseEntity<ApiResponse> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        // Act
        ResponseEntity<ApiResponse> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        List<User> users = new ArrayList<>();
        when(userService.getAllUser()).thenReturn(users);

        // Act
        ResponseEntity<ApiResponse> response = userController.getAllUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        User user = new User();
        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(user);
        when(userService.convertUserToDto(any(User.class))).thenReturn(new UserDto());

        // Act
        ResponseEntity<ApiResponse> response = userController.createUser(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testForgotPassword_Success() {
        // Arrange
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        User user = new User();
        when(userService.forgotPassword(any(ForgotPasswordRequest.class))).thenReturn(user);
        when(userService.convertUserToDto(any(User.class))).thenReturn(new UserDto());

        // Act
        ResponseEntity<ApiResponse> response = userController.forgotPassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully updated Password", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        UserUpdateRequest request = new UserUpdateRequest();
        User user = new User();
        when(userService.updateUser(any(UserUpdateRequest.class), anyLong())).thenReturn(user);
        when(userService.convertUserToDto(any(User.class))).thenReturn(new UserDto());

        // Act
        ResponseEntity<ApiResponse> response = userController.updateUser(request, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully updated User", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testDeleteUser_Success() {
        // Act
        ResponseEntity<ApiResponse> response = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully Deleted User", response.getBody().getMessage());
    }

    @Test
    void testSortUsers_Success() {
        // Arrange
        List<User> users = new ArrayList<>();
        when(userService.sortByField(anyString())).thenReturn(users);

        // Act
        List<User> response = userController.sortUsers("name");

        // Assert
        assertNotNull(response);
    }

    @Test
    void testUserPagination_Success() {
        // Arrange
        List<User> users = new ArrayList<>();
        when(userService.getUserByPagination(anyInt(), anyInt())).thenReturn(new PageImpl<>(users));

        // Act
        List<User> response = userController.userPagination(0, 5);

        // Assert
        assertNotNull(response);
    }
}
