package com.fit.demo.Reservas.Repository;

import com.fit.demo.Reservas.Entidad.Reserva;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    // Buscar reservas por usuario (para próximas reservas y historial)
    List<Reserva> findByIdUsuario(String idUsuario);

    // Buscar reservas por clase
    List<Reserva> findByIdClase(String idClase);

    // Buscar reservas por estado
    List<Reserva> findByEstado(Reserva.EstadoReserva estado);

    // Buscar reservas por usuario y estado
    List<Reserva> findByIdUsuarioAndEstado(String idUsuario, Reserva.EstadoReserva estado);

    // Buscar reservas por usuario y rango de fechas (para historial)
    List<Reserva> findByIdUsuarioAndTimestampCreacionBetween(String idUsuario, LocalDateTime start, LocalDateTime end);

    // Buscar reservas con check-in confirmado (para historial de asistencias)
    List<Reserva> findByIdUsuarioAndConfirmedCheckinTrue(String idUsuario);
}