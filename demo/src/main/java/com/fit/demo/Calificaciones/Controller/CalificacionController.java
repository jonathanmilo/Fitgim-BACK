package com.fit.demo.Calificaciones.Controller;


import com.fit.demo.Calificaciones.Entidad.Calificacion;
import com.fit.demo.Calificaciones.Repository.CalificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionRepository calificacionRepository;

    // Obtener todas las calificaciones
    @GetMapping
    public List<Calificacion> getAllCalificaciones() {
        return calificacionRepository.findAll();
    }

    // Obtener calificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> getCalificacionById(@PathVariable String id) {
        Optional<Calificacion> calificacion = calificacionRepository.findById(id);
        return calificacion.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva calificación
    @PostMapping
    public ResponseEntity<Calificacion> createCalificacion(@Valid @RequestBody Calificacion calificacion) {
        // Validar rango de estrellas (1-5) se maneja vía @Min/@Max en la entidad
        return ResponseEntity.ok(calificacionRepository.save(calificacion));
    }

    // Actualizar una calificación
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> updateCalificacion(@PathVariable String id, @Valid @RequestBody Calificacion calificacionDetails) {
        Optional<Calificacion> calificacion = calificacionRepository.findById(id);
        if (calificacion.isPresent()) {
            Calificacion updatedCalificacion = calificacion.get();
            updatedCalificacion.setIdUsuario(calificacionDetails.getIdUsuario());
            updatedCalificacion.setIdClase(calificacionDetails.getIdClase());
            updatedCalificacion.setIdProfesor(calificacionDetails.getIdProfesor());
            updatedCalificacion.setComentario(calificacionDetails.getComentario());
            updatedCalificacion.setEstrellas(calificacionDetails.getEstrellas());
            updatedCalificacion.setTimestamp(calificacionDetails.getTimestamp());
            return ResponseEntity.ok(calificacionRepository.save(updatedCalificacion));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una calificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable String id) {
        if (calificacionRepository.existsById(id)) {
            calificacionRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar calificaciones por usuario (para historial personal)
    @GetMapping("/usuario/{idUsuario}")
    public List<Calificacion> getCalificacionesByUsuario(@PathVariable String idUsuario) {
        return calificacionRepository.findByIdUsuario(idUsuario);
    }

    // Buscar calificaciones por clase
    @GetMapping("/clase/{idClase}")
    public List<Calificacion> getCalificacionesByClase(@PathVariable String idClase) {
        return calificacionRepository.findByIdClase(idClase);
    }

    // Buscar calificaciones por profesor
    @GetMapping("/profesor/{idProfesor}")
    public List<Calificacion> getCalificacionesByProfesor(@PathVariable String idProfesor) {
        return calificacionRepository.findByIdProfesor(idProfesor);
    }
}