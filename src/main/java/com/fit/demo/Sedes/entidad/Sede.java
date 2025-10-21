package com.fit.demo.Sedes.entidad;




import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sedes") // colección en MongoDB
public class Sede {

    @Id
    private String id_sede;  // Mongo genera _id automáticamente, pero usamos id_sede

    private String nombre;
    private String ubicacion;
    private String barrio;

    // Constructores
    public Sede() {}

    public Sede(String id_sede, String nombre, String ubicacion, String barrio) {
        this.id_sede = id_sede;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.barrio = barrio;
    }

    // Getters y Setters
    public String getId_sede() {
        return id_sede;
    }

    public void setId_sede(String id_sede) {
        this.id_sede = id_sede;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getBarrio() {
        return barrio;
    }
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
