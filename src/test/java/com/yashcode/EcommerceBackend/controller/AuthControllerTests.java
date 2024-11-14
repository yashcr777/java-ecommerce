package com.yashcode.EcommerceBackend.controller;

import com.yashcode.EcommerceBackend.request.LoginRequest;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.response.JwtResponse;
import com.yashcode.EcommerceBackend.security.jwt.JwtUtils;
import com.yashcode.EcommerceBackend.security.user.ShopUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Nested
@SpringBootTest
public class AuthControllerTests {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");
        SecurityContextHolder.setContext(securityContext);
        authController=new AuthController(authenticationManager,jwtUtils);
    }

    @Test
    void testLogin_Success() {
        ShopUserDetails userDetails = new ShopUserDetails(1L, "testuser@example.com", "password", null);
        String jwtToken = "mockJwtToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateTokenForUser(authentication)).thenReturn(jwtToken);

        ResponseEntity<ApiResponse> response = authController.login(loginRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateTokenForUser(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody().getData();
        assertEquals(userDetails.getId(), jwtResponse.getId());
        assertEquals(jwtToken, jwtResponse.getToken());
    }

    @Test
    void testLogin_Failure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<ApiResponse> response = authController.login(loginRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
        assertEquals(null, response.getBody().getData());
    }
}
