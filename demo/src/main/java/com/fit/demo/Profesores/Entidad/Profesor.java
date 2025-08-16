package com.fit.demo.Profesores.Entidad;




import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "profesores")
public class Profesor {
    @Id
    private String idProfesor;
    private String nombre;
    private List<String> clases;
    private List<String> calificaciones;

    // Constructor vacío
    public Profesor() {}

    // Constructor con parámetros
    public Profesor(String idProfesor, String nombre, List<String> clases, List<String> calificaciones) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.clases = clases;
        this.calificaciones = calificaciones;
    }

    // Getters y Setters
    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getClases() {
        return clases;
    }

    public void setClases(List<String> clases) {
        this.clases = clases;
    }

    public List<String> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<String> calificaciones) {
        this.calificaciones = calificaciones;
    }
}