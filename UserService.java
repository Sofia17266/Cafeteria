package com.cafeteria.app.service;

import com.cafeteria.app.dto.UserDTO;
import com.cafeteria.app.mapper.UserMapper;
import com.cafeteria.app.model.Role;
import com.cafeteria.app.model.RoleName;
import com.cafeteria.app.model.User;
import com.cafeteria.app.repository.RoleRepository;
import com.cafeteria.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // method to register an user
    public boolean registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(customerRole);
        userRepository.save(user);
        return true;
    }
    
    // Method to update an user profile
    public boolean updateUserProfile(String currentEmail, UserDTO userDTO) {
    User user = userRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


    user.setName(userDTO.getName());
    user.setPhone(userDTO.getPhone());
    if (!user.getEmail().equals(userDTO.getEmail())) {
       
        boolean emailExists = userRepository.findByEmail(userDTO.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("The email is currently in use by another user place");
        }
        user.setEmail(userDTO.getEmail());
    }

   
    userRepository.save(user);
    return true;
}

    // Method to find an user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // **new methods** fetch a specific user based on their email
    public UserDTO getUserDTOByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found....."));
    return UserMapper.toUserDTO(user);
}

    

    // **Nuevo**: fetch a specific user based on their id
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toUserDTO(user);
    }
}
