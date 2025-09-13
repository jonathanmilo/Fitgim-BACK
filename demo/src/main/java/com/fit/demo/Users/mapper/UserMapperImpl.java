
package com.fit.demo.Users.mapper;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.Users.entidades.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User toUser(RegisterRequest registerRequest) {
        return User.builder()
                .nombre(registerRequest.getNombre().charAt(0)+registerRequest.getEmail())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
