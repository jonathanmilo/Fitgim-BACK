package com.fit.demo.Users;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.Users.entidades.*;
import com.fit.demo.Users.repositry.UserRepository;
 
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
 
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fit.demo.Users.entidades.User;
import com.fit.demo.util.Mapper;
import org.springframework.stereotype.Service;
import com.fit.demo.Users.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.Map;

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

        // Verificar si el nuevo email ya está en uso por otro usuario
        if (!user.getEmail().equals(userDetails.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(userDetails.getEmail());
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + userDetails.getEmail());
            }
        }

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
        // Verificar si ya existe un usuario con ese email
        Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + registerRequest.getEmail());
        }
        
        User user = userMapper.toUser(registerRequest);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public User patchUser(String id, Map<String, Object> updates) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío");
        }

        Optional<User> userOptional = userRepository.findById(new ObjectId(id));

        if (userOptional.isEmpty()) {
            throw new RecursoNoEncontradoException();
        }

        User user = userOptional.get();

        if (updates.containsKey("nombre")) {
            Object value = updates.get("nombre");
            if (value instanceof String) {
                user.setNombre((String) value);
            }
        }
        if (updates.containsKey("email")) {
            Object value = updates.get("email");
            if (value instanceof String) {
                String newEmail = (String) value;
                // Verificar si el nuevo email ya está en uso por otro usuario
                if (!user.getEmail().equals(newEmail)) {
                    Optional<User> existingUser = userRepository.findByEmail(newEmail);
                    if (existingUser.isPresent()) {
                        throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + newEmail);
                    }
                }
                user.setEmail(newEmail);
            }
        }
        if (updates.containsKey("foto")) {
            Object value = updates.get("foto");
            if (value instanceof String) {
                user.setFoto((String) value);
            }
        }
        if (updates.containsKey("reservas")) {
            Object value = updates.get("reservas");
            if (value instanceof java.util.List<?>) {
                @SuppressWarnings("unchecked")
                java.util.List<String> reservas = (java.util.List<String>) value;
                user.setReservas(reservas);
            }
        }
        if (updates.containsKey("password")) {
            Object value = updates.get("password");
            if (value instanceof String) {
                user.setPassword(passwordEncoder.encode((String) value));
            }
        }

        return userRepository.save(user);
    }
}