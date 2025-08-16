package com.fit.demo.Sedes.controller;



import org.springframework.web.bind.annotation.*;

import com.fit.demo.Sedes.entidad.Sede;
import com.fit.demo.Sedes.repository.SedeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sedes")
public class SedeController {

    private final SedeRepository repository;

    public SedeController(SedeRepository repository) {
        this.repository = repository;
    }

    // Obtener todas las sedes
    @GetMapping
    public List<Sede> getAllSedes() {
        return repository.findAll();
    }

    // Obtener una sede por id
    @GetMapping("/{id}")
    public Optional<Sede> getSedeById(@PathVariable String id) {
        return repository.findById(id);
    }

    // Crear una nueva sede
    @PostMapping
    public Sede createSede(@RequestBody Sede sede) {
        return repository.save(sede);
    }

    // Actualizar una sede existente
    @PutMapping("/{id}")
    public Sede updateSede(@PathVariable String id, @RequestBody Sede sede) {
        sede.setId_sede(id);
        return repository.save(sede);
    }

    // Eliminar una sede
    @DeleteMapping("/{id}")
    public void deleteSede(@PathVariable String id) {
        repository.deleteById(id);
    }
}
