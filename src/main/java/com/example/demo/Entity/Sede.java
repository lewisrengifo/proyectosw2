package com.example.demo.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sede")
public class Sede {
    @Id
    private int idrol;
    private String sede;
    private int inventariosede;

    public int getIdrol() {
        return idrol;
    }

    public void setIdrol(int idrol) {
        this.idrol = idrol;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public int getInventariosede() {
        return inventariosede;
    }

    public void setInventariosede(int inventariosede) {
        this.inventariosede = inventariosede;
    }
}
