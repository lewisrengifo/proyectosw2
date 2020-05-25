package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "comunidad")
public class Comunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcomunidad;

    @Column(nullable = false)
    @NotBlank(message = "El texto no puede estar vacio")
    @Size(max = 45, message = "el nombre no puede poseer más de 45 caracteres")
    private String nombrecomunidad;

    @NotBlank(message = "el texto no puede estar vacio")
    @Column(nullable = false)
    @Size(max = 2,message = "debe de tener un máximo de 2 caracteres")
    private String codigocomunidad;

    public int getIdcomunidad() {
        return idcomunidad;
    }

    public void setIdcomunidad(int idcomunidad) {
        this.idcomunidad = idcomunidad;
    }

    public String getNombrecomunidad() {
        return nombrecomunidad;
    }

    public void setNombrecomunidad(String nombrecomunidad) {
        this.nombrecomunidad = nombrecomunidad;
    }

    public String getCodigocomunidad() {
        return codigocomunidad;
    }

    public void setCodigocomunidad(String codigocomunidad) {
        this.codigocomunidad = codigocomunidad;
    }
}
