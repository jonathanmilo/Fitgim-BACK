package com.fit.demo.Users;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.Users.entidades.*;
import com.fit.demo.Users.repositry.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fit.demo.Users.entidades.User;
import com.fit.demo.util.Mapper;
import org.springframework.stereotype.Service;
import com.fit.demo.Users.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

import com.fit.demo.exception.RecursoNoEncontradoException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final Mapper mapper;

    // FIXME: posible exception si userRepository no cuenta con usuarios e intenta hacer stream
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(mapper.mapStringToObjectId(userId)).orElseThrow(RecursoNoEncontradoException::new);
        return userMapper.toUserResponse(user);
    }



    public User updateUser(String id, User userDetails) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }

        Optional<User> userOptional = userRepository.findById(new ObjectId(id));

        if (userOptional.isEmpty()) {
            throw new RecursoNoEncontradoException();
        }

        User user = userOptional.get();

        user.setNombre(userDetails.getNombre());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setFoto(userDetails.getFoto());
        user.setReservas(userDetails.getReservas());
        User updatedUser = userRepository.save(user);

        return user;
       
    }

    public void deleteUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }

        Optional<User> userOptional = userRepository.findById(new ObjectId(id));

        if (userOptional.isEmpty()) {
            throw new RecursoNoEncontradoException();
        }
        userRepository.deleteById(new ObjectId(id));
    }

    public User findUserById(ObjectId id) {
        return userRepository.findById(id).orElseThrow(RecursoNoEncontradoException::new);
    }

    public UserResponse createUser(RegisterRequest registerRequest) {
        User user = userMapper.toUser(registerRequest);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}