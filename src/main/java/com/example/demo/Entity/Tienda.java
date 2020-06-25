package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tienda")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtienda;
    @Column(nullable = false)
    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 45,message = "el texto no puede tener más de 45 caracteres")
    private String nombre;

    public int getIdtienda() {
        return idtienda;
    }

    public void setIdtienda(int idtienda) {
        this.idtienda = idtienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

