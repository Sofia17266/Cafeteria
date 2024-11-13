package com.cafeteria.app.config;

import com.cafeteria.app.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private String name;

    public CustomUserDetails(User user) {
        super(user.getEmail(), user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name())));
        this.name = user.getName();
    }

    public String getName() {
        return name;
    }
}
