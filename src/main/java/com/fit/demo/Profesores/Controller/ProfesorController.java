package com.fit.demo.Profesores.Controller;

import com.fit.demo.Profesores.Entidad.Profesor;
import com.fit.demo.Profesores.Repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorRepository profesorRepository;

    // Obtener todos los profesores
    @GetMapping
    public List<Profesor> getAllProfesores() {
        return profesorRepository.findAll();
    }

    // Obtener profesor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable String id) {
        Optional<Profesor> profesor = profesorRepository.findById(id);
        return profesor.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo profesor
    @PostMapping
    public Profesor createProfesor(@RequestBody Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    // Actualizar un profesor
    @PutMapping("/{id}")
    public ResponseEntity<Profesor> updateProfesor(@PathVariable String id, @RequestBody Profesor profesorDetails) {
        Optional<Profesor> profesor = profesorRepository.findById(id);
        if (profesor.isPresent()) {
            Profesor updatedProfesor = profesor.get();
            updatedProfesor.setNombre(profesorDetails.getNombre());
            updatedProfesor.setClases(profesorDetails.getClases());
            updatedProfesor.setCalificaciones(profesorDetails.getCalificaciones());
            return ResponseEntity.ok(profesorRepository.save(updatedProfesor));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar un profesor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable String id) {
        if (profesorRepository.existsById(id)) {
            profesorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar profesores por nombre
    @GetMapping("/search")
    public List<Profesor> searchProfesores(@RequestParam String nombre) {
        return profesorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Buscar profesores por clase
    @GetMapping("/by-clase/{idClase}")
    public List<Profesor> getProfesoresByClase(@PathVariable String idClase) {
        return profesorRepository.findByClasesContaining(idClase);
    }
}