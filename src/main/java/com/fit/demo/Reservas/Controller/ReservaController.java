package com.fit.demo.Reservas.Controller;

import java.time.LocalDateTime;
import java.util.List;
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

import com.fit.demo.Clases.Entidad.Clase;
import com.fit.demo.Clases.Repository.ClaseRepository;
import com.fit.demo.Reservas.Entidad.Reserva;
import com.fit.demo.Reservas.Entidad.ReservaJoin;
import com.fit.demo.Reservas.Repository.ReservaRepository;
import com.fit.demo.Sedes.entidad.Sede;
import com.fit.demo.Sedes.repository.SedeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private SedeRepository sedeRepository;

    // Obtener todas las reservas
    @GetMapping
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable String id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        return reserva.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<Reserva> createReserva(@Valid @RequestBody Reserva reserva) {
        // Nota: Validar cupo disponible en Clase debería hacerse en un servicio
        Optional<Clase> clase = claseRepository.findById(reserva.getIdClase());
        if (clase.isPresent()) {
            Clase c = clase.get();
            if (c.getCupo() <= 0) {
                return ResponseEntity.badRequest().build(); // No hay cupo disponible
            }
            c.setCupo(c.getCupo() - 1);
            claseRepository.save(c);
        } else {
            return ResponseEntity.badRequest().build(); // Clase no encontrada
        }
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    // Actualizar una reserva
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable String id, @Valid @RequestBody Reserva reservaDetails) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Reserva updatedReserva = reserva.get();
            updatedReserva.setIdClase(reservaDetails.getIdClase());
            updatedReserva.setIdUsuario(reservaDetails.getIdUsuario());
            updatedReserva.setEstado(reservaDetails.getEstado());
            updatedReserva.setTimestampCreacion(reservaDetails.getTimestampCreacion());
            updatedReserva.setTimestampCheckin(reservaDetails.getTimestampCheckin());
            updatedReserva.setConfirmedCheckin(reservaDetails.isConfirmedCheckin());
            return ResponseEntity.ok(reservaRepository.save(updatedReserva));
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable String id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            Optional<Clase> clase = claseRepository.findById(reserva.get().getIdClase());
            if (clase.isPresent()) {
                Clase c = clase.get();
                c.setCupo(c.getCupo() + 1);
                claseRepository.save(c);
            }
            reservaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar reservas por usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaJoin> getReservasByUsuario(@PathVariable String idUsuario) {
        List<Reserva> reservas = reservaRepository.findByIdUsuario(idUsuario);
        List<Clase> clases = claseRepository.findAll();
        List<Sede> sedes = sedeRepository.findAll();

        List<ReservaJoin> reservasConDetalles = reservas.stream().map(reserva -> {
            ReservaJoin reservaJoin = new ReservaJoin();
            reservaJoin.setIdReserva(reserva.getIdReserva());
            reservaJoin.setIdUsuario(reserva.getIdUsuario());
            reservaJoin.setIdClase(reserva.getIdClase());
            reservaJoin.setClase(clases.stream()
                    .filter(c -> c.getIdClase().equals(reserva.getIdClase()))
                    .findFirst()
                    .orElse(null));
            if (reservaJoin.getClase() != null) {
                reservaJoin.setSede(sedes.stream()
                        .filter(s -> s.getId_sede().equals(reservaJoin.getClase().getIdSede()))
                        .findFirst()
                        .orElse(null));
            } else {
                reservaJoin.setSede(null);
            }
            return reservaJoin;
        }).toList();

        return reservasConDetalles;
    }

    // Buscar reservas por clase
    @GetMapping("/clase/{idClase}")
    public List<Reserva> getReservasByClase(@PathVariable String idClase) {
        return reservaRepository.findByIdClase(idClase);
    }

    // Buscar reservas por usuario y estado
    @GetMapping("/usuario/{idUsuario}/estado")
    public List<Reserva> getReservasByUsuarioAndEstado(@PathVariable String idUsuario,
            @RequestParam Reserva.EstadoReserva estado) {
        return reservaRepository.findByIdUsuarioAndEstado(idUsuario, estado);
    }

    // Buscar reservas por usuario y rango de fechas (historial)
    @GetMapping("/usuario/{idUsuario}/historial")
    public List<Reserva> getHistorialByUsuarioAndFechas(
            @PathVariable String idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return reservaRepository.findByIdUsuarioAndTimestampCreacionBetween(idUsuario, start, end);
    }

    // Buscar asistencias confirmadas por usuario (historial)
    @GetMapping("/usuario/{idUsuario}/asistencias")
    public List<Reserva> getAsistenciasByUsuario(@PathVariable String idUsuario) {
        return reservaRepository.findByIdUsuarioAndConfirmedCheckinTrue(idUsuario);
    }
}