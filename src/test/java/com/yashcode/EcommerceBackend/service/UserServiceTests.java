package com.yashcode.EcommerceBackend.service;

import com.yashcode.EcommerceBackend.Repository.RoleRepository;
import com.yashcode.EcommerceBackend.Repository.UserRepository;
import com.yashcode.EcommerceBackend.dto.UserDto;
import com.yashcode.EcommerceBackend.entity.Role;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.ForgotPasswordRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import com.yashcode.EcommerceBackend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private CreateUserRequest createUserRequest;
    private ForgotPasswordRequest forgotPasswordRequest;
    private UserUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        role = new Role();
        role.setName("ROLE_USER");

        createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("newuser@example.com");
        createUserRequest.setPassword("password");
        createUserRequest.setFirstName("New");
        createUserRequest.setLastName("User");

        forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("test@example.com");
        forgotPasswordRequest.setPassword("newPassword");

        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setFirstName("UpdatedFirstName");
        userUpdateRequest.setLastName("UpdatedLastName");
    }

    @Test
    void testGetUserById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetAllUsers() {
        List<User> userList = List.of(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAllUser();
        assertEquals(userList, result);
    }

    @Test
    void testForgotPassword_UserFound() {
        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(user);
        when(passwordEncoder.encode(forgotPasswordRequest.getPassword())).thenReturn("encodedPassword");
        User updatedUser = userService.forgotPassword(forgotPasswordRequest);
        assertEquals("encodedPassword", updatedUser.getPassword());
    }

    @Test
    void testForgotPassword_UserNotFound() {
        when(userRepository.findByEmail(forgotPasswordRequest.getEmail())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userService.forgotPassword(forgotPasswordRequest));
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(createUserRequest.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(createUserRequest);
        assertEquals(user.getEmail(), createdUser.getEmail());
    }

    @Test
    void testCreateUser_AlreadyExists() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(true);
        assertThrows(AlreadyExistException.class, () -> userService.createUser(createUserRequest));
    }

    @Test
    void testUpdateUser_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = userService.updateUser(userUpdateRequest, 1L);
        assertEquals(userUpdateRequest.getFirstName(), updatedUser.getFirstName());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userUpdateRequest, 1L));
    }

    @Test
    void testDeleteUser_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);
        userService.deletedUser(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.deletedUser(1L));
    }

    @Test
    void testConvertUserToDto() {
        UserDto userDto = new UserDto();
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        UserDto result = userService.convertUserToDto(user);
        assertEquals(userDto, result);
    }

    @Test
    void testGetAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User authenticatedUser = userService.getAuthenticatedUser();
        assertNotNull(authenticatedUser);
    }

    @Test
    void testSortByField() {
        List<User> userList = List.of(user);
        when(userRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName"))).thenReturn(userList);
        List<User> result = userService.sortByField("firstName");
        assertEquals(userList, result);
    }

    @Test
    void testGetUserByPagination() {
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        Page<User> result = userService.getUserByPagination(0, 10);
        assertEquals(page, result);
    }
}