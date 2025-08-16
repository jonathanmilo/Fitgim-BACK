package com.fit.demo.Users.repositry;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fit.demo.Users.entidades.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByNombreAndEmail (String nombre, String email);

}

