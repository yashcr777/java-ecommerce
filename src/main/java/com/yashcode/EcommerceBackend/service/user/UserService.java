package com.yashcode.EcommerceBackend.service.user;

import com.yashcode.EcommerceBackend.Repository.UserRepository;
import com.yashcode.EcommerceBackend.dto.CreatedUserDto;
import com.yashcode.EcommerceBackend.dto.UserDto;
import com.yashcode.EcommerceBackend.dto.UserUpdateDto;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        System.out.println(userId);
        return userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
    }
    @Override
    public List<User> getAllUser(){
       return  userRepository.findAll();
    }
    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user=new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistException("Oops!"+request.getEmail()+"already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new ResourceNotFoundException("User not Found!"));
    }

    @Override
    public void deletedUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()->{
            throw new ResourceNotFoundException("User not Found!");
        });
    }
    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        String email=authentication.getName();
        System.out.println(email);
        return userRepository.findByEmail(email);
    }
}
