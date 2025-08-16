package com.fit.demo.Clases.Entidad;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "clases")
public class Clase {
    @Id
    private String idClase;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private int duracion;
    private String disciplina;
    private String idSede;
    private LocalDate fecha;
    private String idProfesor;
    private int cupo;
    private EstadoClase estado;
    private List<String> calificaciones;

    // Enum para estado
    public enum EstadoClase {
        CONFIRMADA, CANCELADA, EXPIRADA, CREADA
    }

    // Constructor vacío
    public Clase() {}

    // Constructor con parámetros
    public Clase(String idClase, LocalTime horarioInicio, LocalTime horarioFin, int duracion, String disciplina, 
                 String idSede, LocalDate fecha, String idProfesor, int cupo, EstadoClase estado, 
                 List<String> calificaciones) {
        this.idClase = idClase;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.duracion = duracion;
        this.disciplina = disciplina;
        this.idSede = idSede;
        this.fecha = fecha;
        this.idProfesor = idProfesor;
        this.cupo = cupo;
        this.estado = estado;
        this.calificaciones = calificaciones;
    }

    // Getters y Setters
    public String getIdClase() {
        return idClase;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getIdSede() {
        return idSede;
    }

    public void setIdSede(String idSede) {
        this.idSede = idSede;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public EstadoClase getEstado() {
        return estado;
    }

    public void setEstado(EstadoClase estado) {
        this.estado = estado;
    }

    public List<String> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<String> calificaciones) {
        this.calificaciones = calificaciones;
    }
}