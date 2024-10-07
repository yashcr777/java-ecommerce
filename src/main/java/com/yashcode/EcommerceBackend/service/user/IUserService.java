package com.yashcode.EcommerceBackend.service.user;

import com.yashcode.EcommerceBackend.dto.CreatedUserDto;
import com.yashcode.EcommerceBackend.dto.UserDto;
import com.yashcode.EcommerceBackend.dto.UserUpdateDto;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;

import java.util.List;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deletedUser(Long userId);
    UserDto convertUserToDto(User user);
    List<User> getAllUser();

    User getAuthenticatedUser();
}
