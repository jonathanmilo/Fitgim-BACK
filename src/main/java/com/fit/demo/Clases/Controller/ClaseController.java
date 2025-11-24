package com.fit.demo.Clases.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fit.demo.Calificaciones.Entidad.Calificacion;
import com.fit.demo.Calificaciones.Repository.CalificacionRepository;
import com.fit.demo.Clases.Entidad.Clase;
import com.fit.demo.Clases.Repository.ClaseRepository;
import com.fit.demo.Profesores.Entidad.Profesor;
import com.fit.demo.Profesores.Repository.ProfesorRepository;
import com.fit.demo.Sedes.entidad.Sede;
import com.fit.demo.Sedes.repository.SedeRepository;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private CalificacionRepository calificacionRepository;

    // Obtener todas las clases
    @GetMapping
    public List<Clase> getAllClases() {
        System.out.println("entró a getAllClases");
        return claseRepository.findAll();
    }

    // Obtener clase por ID
    // Por alguna razon los join son horribles en Mongo
    @GetMapping("/{id}")
    public ResponseEntity<?> getClaseById(@PathVariable String id) {
        Optional<Clase> claseOpt = claseRepository.findById(id);

        if (claseOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Clase clase = claseOpt.get();
        Sede sede = sedeRepository.findById(clase.getIdSede()).orElse(null);
        Profesor profesor = profesorRepository.findById(clase.getIdProfesor()).orElse(null);

        List<Calificacion> calificaciones = calificacionRepository.findByIdClase(clase.getIdClase());

        Map<String, Object> result = new HashMap<>();
        result.put("idClase", clase.getIdClase());
        result.put("disciplina", clase.getDisciplina());
        result.put("fecha", clase.getFecha());
        result.put("cupo", clase.getCupo());
        result.put("horarioInicio", clase.getHorarioInicio());
        result.put("horarioFin", clase.getHorarioFin());
        result.put("calificaciones", calificaciones);
        result.put("nombreSede", sede != null ? sede.getNombre() : null);
        result.put("ubicacionSede", sede != null ? sede.getUbicacion() : null);
        result.put("nombreProfesor", profesor != null ? profesor.getNombre() : null);

        return ResponseEntity.ok(result);
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

    // Buscar clases por sede nombre
    @GetMapping("/search/sede")
    public List<Clase> searchBySedeName(@RequestParam String sedeName) {
        Sede sede = sedeRepository.findByNombre(sedeName);

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