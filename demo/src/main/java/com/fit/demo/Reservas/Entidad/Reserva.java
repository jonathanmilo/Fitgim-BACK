package com.fit.demo.Reservas.Entidad;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "reservas")
public class Reserva {
    @Id
    private String idReserva;
    @NotNull
    private String idClase;
    @NotNull
    private String idUsuario;
    private EstadoReserva estado;
    @NotNull
    private LocalDateTime timestampCreacion;
    private LocalDateTime timestampCheckin; // Opcional
    private boolean confirmedCheckin;

    // Enum para estado
    public enum EstadoReserva {
        CONFIRMADA, CANCELADA, EXPIRADA
    }

    // Constructor vacío
    public Reserva() {}

    // Constructor con parámetros
    public Reserva(String idReserva, String idClase, String idUsuario, EstadoReserva estado, 
                   LocalDateTime timestampCreacion, LocalDateTime timestampCheckin, boolean confirmedCheckin) {
        this.idReserva = idReserva;
        this.idClase = idClase;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.timestampCreacion = timestampCreacion;
        this.timestampCheckin = timestampCheckin;
        this.confirmedCheckin = confirmedCheckin;
    }

    // Getters y Setters
    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getTimestampCreacion() {
        return timestampCreacion;
    }

    public void setTimestampCreacion(LocalDateTime timestampCreacion) {
        this.timestampCreacion = timestampCreacion;
    }

    public LocalDateTime getTimestampCheckin() {
        return timestampCheckin;
    }

    public void setTimestampCheckin(LocalDateTime timestampCheckin) {
        this.timestampCheckin = timestampCheckin;
    }

    public boolean isConfirmedCheckin() {
        return confirmedCheckin;
    }

    public void setConfirmedCheckin(boolean confirmedCheckin) {
        this.confirmedCheckin = confirmedCheckin;
    }
}