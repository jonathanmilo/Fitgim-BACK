package com.fit.demo.Users.repositry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fit.demo.Users.entidades.User;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    Optional<User>  findByEmail(String email);
    Optional<User> findByNombreAndEmail (String nombre, String email);
    Optional<User>  findByNombre(String nombre);
    Optional<User>  findByPassword(String password);
    Optional<User> findById(ObjectId id);
    Optional<User>  deleteById(ObjectId id);

}

