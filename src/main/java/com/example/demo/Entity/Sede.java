package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.stream.IntStream;

@Entity
@Table(name = "sede")
public class Sede implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsede")
    private int idsede;
    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre no debe ser vacío")
    @Pattern(regexp="[a-zA-ZÀ-ÿ\\u00f1\\u00d1]{1,45}",message = "Solo aceptan letras")
    private String nombre;

    public int getIdsede() {
        return idsede;
    }

    public void setIdsede(int idsede) {
        this.idsede = idsede;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
