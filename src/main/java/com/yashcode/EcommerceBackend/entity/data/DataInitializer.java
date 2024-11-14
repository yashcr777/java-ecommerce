package com.yashcode.EcommerceBackend.entity.data;


import com.yashcode.EcommerceBackend.Repository.RoleRepository;
import com.yashcode.EcommerceBackend.Repository.UserRepository;
import com.yashcode.EcommerceBackend.entity.Role;
import com.yashcode.EcommerceBackend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String>defaultRoles=Set.of("Role_ADMIN","ROLE_USER");
//        createDefaultUserIfNotExists();
//        createDefaultRoleIfNotExists(defaultRoles);
//        createDefaultAdminIfNotExists();
    }
    private void createDefaultUserIfNotExists(){
        Role userRole=roleRepository.findByName("ROLE_USER").get();
        System.out.println(userRole);
        for(int i=7;i<=10;i++)
        {
            String defaultEmail="user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user=new User();
            user.setFirstName("The User");
            user.setLastName("User"+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user "+i+" created successfully");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role->roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }



    private void createDefaultAdminIfNotExists(){
        Role adminRole=roleRepository.findByName("Role_ADMIN").get();
        for(int i=1;i<=2;i++)
        {
            String defaultEmail="admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user=new User();
            user.setFirstName("Admin");
            user.setLastName("Admin"+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user "+i+" created successfully");
        }
    }
}