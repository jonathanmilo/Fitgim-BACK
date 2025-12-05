package com.fit.demo.Clases.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fit.demo.Clases.Entidad.Clase;

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

        @Aggregation(pipeline = {
                        "{ '$match': { 'idClase': ?0 } }",
                        "{ '$lookup': { 'from': 'sedes', 'localField': 'idSede', 'foreignField': 'id_sede', 'as': 'sede' } }",
                        "{ '$unwind': { 'path': '$sede', 'preserveNullAndEmptyArrays': true } }",
                        "{ '$lookup': { 'from': 'profesores', 'localField': 'idProfesor', 'foreignField': 'idProfesor', 'as': 'profesor' } }",
                        "{ '$unwind': { 'path': '$profesor', 'preserveNullAndEmptyArrays': true } }",
                        "{ '$project': { " +
                                        "'_id': 1, " +
                                        "'disciplina': 1, " +
                                        "'fecha': 1, " +
                                        "'horarioInicio': 1, " +
                                        "'horarioFin': 1, " +
                                        "'nombreSede': '$sede.nombre', " +
                                        "'ubicacionSede': '$sede.ubicacion', " +
                                        "'nombreProfesor': '$profesor.nombre' " +
                                        "} }"
        })
        org.bson.Document findFullByIdClase(String idClase);
}