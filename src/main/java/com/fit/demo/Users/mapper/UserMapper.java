
package com.fit.demo.Users.mapper;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.Users.entidades.User;

public interface UserMapper {
    User toUser(RegisterRequest registerRequest);
    UserResponse toUserResponse(User user);
}
