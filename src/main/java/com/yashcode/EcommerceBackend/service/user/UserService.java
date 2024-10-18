package com.yashcode.EcommerceBackend.service.user;

import com.yashcode.EcommerceBackend.Repository.RoleRepository;
import com.yashcode.EcommerceBackend.Repository.UserRepository;

import com.yashcode.EcommerceBackend.dto.ProductDto;
import com.yashcode.EcommerceBackend.dto.UserDto;

import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.entity.Role;
import com.yashcode.EcommerceBackend.entity.User;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.request.CreateUserRequest;
import com.yashcode.EcommerceBackend.request.ForgotPasswordRequest;
import com.yashcode.EcommerceBackend.request.UserUpdateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {



    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->{
                    log.error("User with the given id not found");
                    return new ResourceNotFoundException("User Not Found!");
                });
    }


    @Override
    public List<User> getAllUser(){
       return  userRepository.findAll();
    }


    @Override
    public User forgotPassword(ForgotPasswordRequest request){
        return Optional.ofNullable(userRepository.findByEmail(request.getEmail()))
                .map(req->{
                    User user=userRepository.findByEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(()->{
                    log.error("User not Found");
                    return new UsernameNotFoundException("User not found");
                });

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
                    Role userRole=roleRepository.findByName("ROLE_USER").get();
                    user.setRoles(Set.of(userRole));
                    return userRepository.save(user);
                }).orElseThrow(()-> new AlreadyExistException("Oops!"+request.getEmail()+"already exists!"));
    }


    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            log.info("User updated Successfully");
            return userRepository.save(existingUser);
        }).orElseThrow(()->{
            log.error("User is not found");
            return new ResourceNotFoundException("User not Found!");
        });
    }


    @Override
    @Transactional
    public void deletedUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()->{
            log.info("User is not present");
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
    @Override
    public List<User>sortByField(String field){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC,field));
    }
    @Override
    public List<User>sortByFieldDesc(String field){
        return userRepository.findAll((Sort.by(Sort.Direction.DESC,field)));
    }
    @Override
    public Page<User> getUserByPagination(int offset, int pageSize){
        return userRepository.findAll(PageRequest.of(offset,pageSize));
    }
    @Override
    public Page<User> getUserByPaginationAndSorting(int offset, int pageSize,String field){
        return userRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(Sort.Direction.DESC,field)));
    }
}
