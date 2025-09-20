package com.fit.demo.Clases.Controller;


import com.fit.demo.Clases.Entidad.Clase;
import com.fit.demo.Clases.Repository.ClaseRepository;
import com.fit.demo.Sedes.entidad.Sede;
import com.fit.demo.Sedes.repository.SedeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    @Autowired
    private ClaseRepository claseRepository;

    private SedeRepository sedeRepository;

    // Obtener todas las clases
    @GetMapping
    public List<Clase> getAllClases() {
        System.out.println("entró a getAllClases");
        return claseRepository.findAll();
    }

    // Obtener clase por ID
    @GetMapping("/{id}")
    public ResponseEntity<Clase> getClaseById(@PathVariable String id) {
        Optional<Clase> clase = claseRepository.findById(id);
        return clase.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva clase
    @PostMapping
    public Clase createClase(@RequestBody Clase clase) {
        return claseRepository.save(clase);
    }

    // Actualizar una clase
    @PutMapping("/{id}")
    public ResponseEntity<Clase> updateClase(@PathVariable String id, @RequestBody Clase claseDetails) {
        Optional<Clase> clase = claseRepository.findById(id);
        if (clase.isPresent()) {
            Clase updatedClase = clase.get();
            updatedClase.setHorarioInicio(claseDetails.getHorarioInicio());
            updatedClase.setHorarioFin(claseDetails.getHorarioFin());
            updatedClase.setDuracion(claseDetails.getDuracion());
            updatedClase.setDisciplina(claseDetails.getDisciplina());
            updatedClase.setIdSede(claseDetails.getIdSede());
            updatedClase.setFecha(claseDetails.getFecha());
            updatedClase.setIdProfesor(claseDetails.getIdProfesor());
            updatedClase.setCupo(claseDetails.getCupo());
            updatedClase.setEstado(claseDetails.getEstado());
            updatedClase.setCalificaciones(claseDetails.getCalificaciones());
            return ResponseEntity.ok(claseRepository.save(updatedClase));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una clase
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClase(@PathVariable String id) {
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar clases por disciplina
    @GetMapping("/search/disciplina")
    public List<Clase> searchByDisciplina(@RequestParam String disciplina) {
        return claseRepository.findByDisciplinaContainingIgnoreCase(disciplina);
    }

    // Buscar clases por sede
    @GetMapping("/search/sedeId")
    public List<Clase> searchBySedeID(@RequestParam String idSede) {
        return claseRepository.findByIdSede(idSede);
    }

    //Buscar clases por sede nombre
    @GetMapping("/search/sede")
    public List<Clase> searchBySedeName(@RequestParam String sedeName)
    {
        Sede sede=sedeRepository.findByNombre(sedeName);
    
        return claseRepository.findByIdSede(sede.getId_sede());
    }
    

    // Buscar clases por fecha
    @GetMapping("/search/fecha")
    public List<Clase> searchByFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return claseRepository.findByFecha(fecha);
    }

    // Buscar clases por filtros combinados (disciplina, sede, fecha)
    @GetMapping("/search")
    public List<Clase> searchClases(
            @RequestParam(required = false) String disciplina,
            @RequestParam(required = false) String idSede,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (disciplina != null && idSede != null && fecha != null) {
            return claseRepository.findByDisciplinaContainingIgnoreCaseAndIdSedeAndFecha(disciplina, idSede, fecha);
        } else if (disciplina != null) {
            return claseRepository.findByDisciplinaContainingIgnoreCase(disciplina);
        } else if (idSede != null) {
            return claseRepository.findByIdSede(idSede);
        } else if (fecha != null) {
            return claseRepository.findByFecha(fecha);
        }
        return claseRepository.findAll();
    }
}