package com.fit.demo.Calificaciones.Repository;

import com.fit.demo.Calificaciones.Entidad.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CalificacionRepository extends MongoRepository<Calificacion, String> {
    // Buscar calificaciones por usuario (para historial personal)
    List<Calificacion> findByIdUsuario(String idUsuario);

    // Buscar calificaciones por clase
    List<Calificacion> findByIdClase(String idClase);

    // Buscar calificaciones por profesor
    List<Calificacion> findByIdProfesor(String idProfesor);

    // Buscar calificaciones por usuario y clase
    List<Calificacion> findByIdUsuarioAndIdClase(String idUsuario, String idClase);
}