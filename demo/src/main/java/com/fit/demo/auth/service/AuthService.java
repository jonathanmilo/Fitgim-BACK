package com.fit.demo.auth.service;

import java.time.Instant;
import com.fit.demo.auth.dto.RegisterRequest;
import com.fit.demo.auth.dto.TokenResponse;
import com.fit.demo.Users.entidades.UserResponse;
import com.fit.demo.auth.dto.LoginRequest;
import com.fit.demo.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import com.fit.demo.Users.entidades.User;
import com.fit.demo.Users.repositry.UserRepository;
import com.fit.demo.Users.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fit.demo.exception.RecursoNoEncontradoException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(RecursoNoEncontradoException::new);

        System.out.println("EMAIL: "+request.getEmail()+" PASSWORD: "+request.getPassword());
        System.out.println("USER PASSWORD: "+user.getPassword());
    if (!isPasswordEncrypted(user.getPassword())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
   
    if (passwordEncoder.matches(request.getPassword(), user.getPassword()) || 
        request.getPassword().equals(user.getPassword())) {
        // Si la contraseña no está encriptada, encriptarla y actualizarla
        if (!isPasswordEncrypted(user.getPassword())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user); // Guardar la contraseña encriptada
        }
    } else {
        throw new RuntimeException("Credenciales inválidas");
    }

    String accessToken = jwtUtil.generateToken(user.getNombre());
    String refreshToken = jwtUtil.generateToken(user.getNombre());
    String userId = user.getId().toString();
    String nombre = user.getNombre();
    String email = user.getEmail();


    return new TokenResponse(accessToken, refreshToken,
            userId, nombre, email);
}

    public UserResponse createUser(RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

 public TokenResponse refreshToken(String refreshToken) {
        try {
            User user = jwtUtil.getUserFromToken(refreshToken,userRepository);
            if (user == null) {
                throw new RuntimeException("Usuario no encontrado");
            }

        String newaccessToken = jwtUtil.generateToken(user.getNombre());
        String newrefreshToken = jwtUtil.generateToken(user.getNombre());
        String userId = user.getId().toString();
        String nombre = user.getNombre();
        String email = user.getEmail();




        return new TokenResponse(newaccessToken, newrefreshToken,
                userId, nombre, email);


        } catch (Exception e) {
            throw new RuntimeException("No se pudo refrescar el token: " + e.getMessage());
        }
    }
    private boolean isPasswordEncrypted(String password) {
    // Una heurística simple: si no tiene un formato de hash (e.g., $2a$ para BCrypt), asumimos que no está encriptada
    return password != null && password.matches("^\\$2[ayb]\\$.+");
}
}
