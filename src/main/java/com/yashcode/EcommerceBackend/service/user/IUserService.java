package com.yashcode.EcommerceBackend.service.user;


import com.yashcode.EcommerceBackend.entity.dto.UserDto;

import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.ForgotPasswordRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deletedUser(Long userId);
    UserDto convertUserToDto(User user);
    List<User> getAllUser();
    User forgotPassword(ForgotPasswordRequest request);

    User getAuthenticatedUser();
    List<User>sortByField(String field);
    List<User>sortByFieldDesc(String field);
    Page<User> getUserByPagination(int offset, int pageSize);
    Page<User> getUserByPaginationAndSorting(int offset, int pageSize,String field);
}
