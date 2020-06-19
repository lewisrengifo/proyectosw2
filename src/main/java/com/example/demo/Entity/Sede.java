package com.example.demo.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.stream.IntStream;

@Entity
@Table(name = "sede")
public class Sede implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrol")
    private int idrol;
    @Column(name = "sede", nullable = false)
    private String sede;
    @Column(name = "iventariosede_idiventariosede")
    private Integer iventariosede_idiventariosede;

    public Integer getIventariosede_idiventariosede() {
        return iventariosede_idiventariosede;
    }

    public void setIventariosede_idiventariosede(Integer iventariosede_idiventariosede) {
        this.iventariosede_idiventariosede = iventariosede_idiventariosede;
    }


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

}
