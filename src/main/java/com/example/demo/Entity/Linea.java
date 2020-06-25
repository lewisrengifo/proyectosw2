package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "linea")
public class Linea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idlinea;
    @Column(nullable = false)
    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 45,message = "el texto no puede tener más de 45 caracteres")
    private String nombrelinea;
    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 45,message = "el texto no puede tener más de 45 caracteres")
    @Column(nullable = false)
    private String codigolinea;

    public int getIdlinea() {
        return idlinea;
    }

    public void setIdlinea(int idlinea) {
        this.idlinea = idlinea;
    }

    public String getNombrelinea() {
        return nombrelinea;
    }

    public void setNombrelinea(String nombrelinea) {
        this.nombrelinea = nombrelinea;
    }

    public String getCodigolinea() {
        return codigolinea;
    }

    public void setCodigolinea(String codigolinea) {
        this.codigolinea = codigolinea;
    }
}
