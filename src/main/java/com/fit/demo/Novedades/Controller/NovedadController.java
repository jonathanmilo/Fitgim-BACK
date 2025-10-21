package com.fit.demo.Novedades.Controller;


import com.fit.demo.Novedades.Entidad.Novedad;
import com.fit.demo.Novedades.Entidad.Novedad.TipoNovedad;
import com.fit.demo.Novedades.Repository.NovedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/novedades")
public class NovedadController {

    @Autowired
    private NovedadRepository novedadRepository;

    // Obtener todas las novedades
    @GetMapping
    public List<Novedad> getAllNovedades() {
        return novedadRepository.findAll();
    }

    // Obtener novedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Novedad> getNovedadById(@PathVariable String id) {
        Optional<Novedad> novedad = novedadRepository.findById(id);
        return novedad.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva novedad
    @PostMapping
    public ResponseEntity<Novedad> createNovedad(@Valid @RequestBody Novedad novedad) {
        return ResponseEntity.ok(novedadRepository.save(novedad));
    }

    // Actualizar una novedad
    @PutMapping("/{id}")
    public ResponseEntity<Novedad> updateNovedad(@PathVariable String id, @Valid @RequestBody Novedad novedadDetails) {
        Optional<Novedad> novedad = novedadRepository.findById(id);
        if (novedad.isPresent()) {
            Novedad updatedNovedad = novedad.get();
            updatedNovedad.setTitulo(novedadDetails.getTitulo());
            updatedNovedad.setContenido(novedadDetails.getContenido());
            updatedNovedad.setTipo(novedadDetails.getTipo());
            updatedNovedad.setFecha(novedadDetails.getFecha());
            return ResponseEntity.ok(novedadRepository.save(updatedNovedad));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una novedad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNovedad(@PathVariable String id) {
        if (novedadRepository.existsById(id)) {
            novedadRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar novedades por tipo
    @GetMapping("/tipo/{tipo}")
    public List<Novedad> getNovedadesByTipo(@PathVariable TipoNovedad tipo) {
        return novedadRepository.findByTipo(tipo);
    }

    // Buscar novedades por fecha
    @GetMapping("/fecha")
    public List<Novedad> getNovedadesByFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return novedadRepository.findByFecha(fecha);
    }

    // Buscar novedades por tipo y fecha
    @GetMapping("/search")
    public List<Novedad> searchNovedades(
            @RequestParam(required = false) TipoNovedad tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (tipo != null && fecha != null) {
            return novedadRepository.findByTipoAndFecha(tipo, fecha);
        } else if (tipo != null) {
            return novedadRepository.findByTipo(tipo);
        } else if (fecha != null) {
            return novedadRepository.findByFecha(fecha);
        }
        return novedadRepository.findAll();
    }
}