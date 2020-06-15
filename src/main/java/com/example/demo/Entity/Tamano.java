package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tamano")
public class Tamano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtamano;
    @Column(nullable = false)
    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 45,message = "el texto no puede tener más de 45 caracteres")
    private String nombretamano;
    @Column(nullable = false)
    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 3,message = "el texto no puede tener más de 3 caracteres")
    private String codigotamano;

    public int getIdtamano() {
        return idtamano;
    }

    public void setIdtamano(int idtamano) {
        this.idtamano = idtamano;
    }

    public String getNombretamano() {
        return nombretamano;
    }

    public void setNombretamano(String nombretamano) {
        this.nombretamano = nombretamano;
    }

    public String getCodigotamano() {
        return codigotamano;
    }

    public void setCodigotamano(String codigotamano) {
        this.codigotamano = codigotamano;
    }
}
