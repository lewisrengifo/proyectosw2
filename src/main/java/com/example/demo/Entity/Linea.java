package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "linea")
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idlinea;
    @Column(nullable = false)
    private String nombrelinea;
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
