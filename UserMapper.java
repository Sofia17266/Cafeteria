package com.cafeteria.app.mapper;

import com.cafeteria.app.dto.UserDTO;
import com.cafeteria.app.model.User;

public interface UserMapper {

    public static UserDTO toUserDTO(User user) {
        
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone()
            
        );
    }
}
