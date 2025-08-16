package com.fit.demo.Calificaciones.Entidad;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

@Document(collection = "calificaciones")
public class Calificacion {
    @Id
    private String idCalificacion;
    private String idUsuario;
    private String idClase;
    private String idProfesor; // Opcional
    private String comentario; // Opcional
    @Min(1)
    @Max(5)
    private int estrellas;
    private LocalDateTime timestamp;

    // Constructor vacío
    public Calificacion() {}

    // Constructor con parámetros
    public Calificacion(String idCalificacion, String idUsuario, String idClase, String idProfesor, 
                        String comentario, int estrellas, LocalDateTime timestamp) {
        this.idCalificacion = idCalificacion;
        this.idUsuario = idUsuario;
        this.idClase = idClase;
        this.idProfesor = idProfesor;
        this.comentario = comentario;
        this.estrellas = estrellas;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(String idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}