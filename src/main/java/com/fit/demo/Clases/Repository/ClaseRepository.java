package com.fit.demo.Clases.Repository;

import com.fit.demo.Clases.Entidad.Clase;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ClaseRepository extends MongoRepository<Clase, String> {
    // Buscar clases por disciplina (insensible a mayúsculas/minúsculas)
    List<Clase> findByDisciplinaContainingIgnoreCase(String disciplina);

    // Buscar clases por sede
    List<Clase> findByIdSede(String idSede);

    // Buscar clases por fecha
    List<Clase> findByFecha(LocalDate fecha);

    // Buscar clases por profesor
    List<Clase> findByIdProfesor(String idProfesor);

    // Buscar clases por estado
    List<Clase> findByEstado(Clase.EstadoClase estado);

    // Buscar clases por disciplina, sede y fecha (para catálogo filtrado)
    List<Clase> findByDisciplinaContainingIgnoreCaseAndIdSedeAndFecha(
        String disciplina, String idSede, LocalDate fecha);
}