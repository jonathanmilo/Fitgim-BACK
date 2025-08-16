package com.fit.demo.Sedes.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.fit.demo.Sedes.entidad.Sede;

public interface SedeRepository extends MongoRepository<Sede, String> {
    // Puedes agregar métodos personalizados si quieres
    Sede findByNombre(String nombre);
}