package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "comunidad")
public class Comunidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcomunidad;

    @Column(nullable = false)
    @NotBlank(message = "El texto no puede estar en blanco")
    /*@Size(max = 45, message = "El nombre no puede exceder más de 45 caracteres")*/

    @Pattern(regexp="[a-zñÑA-Záéíóú]{1,45}",message = "Solo aceptan letras y un maximo de 45 caracteres")

    private String nombrecomunidad;

    @NotBlank(message = "El texto no puede estar en blanco")
    @Column(nullable = false)
    /*@Size(min = 2,max = 2,message = "Debe tener 2 caracteres")*/

    @Pattern(regexp="[a-zñÑA-Z]{2}",message = "Solo aceptan 2 letras y sin tildes")

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
