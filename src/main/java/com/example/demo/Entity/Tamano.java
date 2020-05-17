package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "tamano")
public class Tamano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtamano;
    @Column(nullable = false)
    private String nombretamano;
    @Column(nullable = false)
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
