package com.fit.demo.Reservas.Controller;

import com.fit.demo.Reservas.Entidad.Reserva;
import com.fit.demo.Reservas.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

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
        if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar reservas por usuario
    @GetMapping("/usuario/{idUsuario}")
    public List<Reserva> getReservasByUsuario(@PathVariable String idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario);
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