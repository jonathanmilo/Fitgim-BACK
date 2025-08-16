package com.fit.demo.Novedades.Entidad;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "novedades")
public class Novedad {
    @Id
    private String idNovedad;
    private String titulo;
    private String contenido;
    private TipoNovedad tipo;
    private LocalDate fecha;

    // Enum para tipo
    public enum TipoNovedad {
        NOTICIA, PROMO, EVENTO
    }

    // Constructor vacío
    public Novedad() {}

    // Constructor con parámetros
    public Novedad(String idNovedad, String titulo, String contenido, TipoNovedad tipo, LocalDate fecha) {
        this.idNovedad = idNovedad;
        this.titulo = titulo;
        this.contenido = contenido;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    // Getters y Setters
    public String getIdNovedad() {
        return idNovedad;
    }

    public void setIdNovedad(String idNovedad) {
        this.idNovedad = idNovedad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public TipoNovedad getTipo() {
        return tipo;
    }

    public void setTipo(TipoNovedad tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}