package com.fit.demo.Users.entidades;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String nombre;
    private String email;
    private String password;

}