package com.fit.demo.Users.entidades;


import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "users")  // colección en MongoDB
public class User {

    @Id
    private String id;
    private String nombre;
    private String email;
    private String foto;
    private List<String> reservas;
    private String password;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFoto(){return foto;}
    public void setFoto(String foto_url){this.foto=foto_url;}

    public List<String> getReservas() {return reservas;}
    public void setReservas(List<String> reservas){this.reservas=reservas;}

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }



}