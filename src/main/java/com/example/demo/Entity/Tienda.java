package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "tienda")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtienda;
    @Column(nullable = false)
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

