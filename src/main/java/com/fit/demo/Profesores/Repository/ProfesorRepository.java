package com.fit.demo.Profesores.Repository;


import com.fit.demo.Profesores.Entidad.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProfesorRepository extends MongoRepository<Profesor, String> {
    // Buscar profesores por nombre (insensible a mayúsculas/minúsculas)
    List<Profesor> findByNombreContainingIgnoreCase(String nombre);

    // Buscar profesores por clase específica
    List<Profesor> findByClasesContaining(String idClase);
}