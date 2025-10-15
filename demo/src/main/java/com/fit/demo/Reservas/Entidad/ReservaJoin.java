package com.fit.demo.Reservas.Entidad;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fit.demo.Clases.Entidad.Clase;
import com.fit.demo.Sedes.entidad.Sede;

public class ReservaJoin {
    // Reserva fields you want back (add/remove as needed)
    @Field("_id ")
    private String idReserva;
    private String idUsuario;
    private String idClase;

    // Joined docs
    private Clase clase;
    private Sede sede;

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
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

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    // getters/setters …
}
