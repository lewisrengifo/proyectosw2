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

    public int getIventariosede_idiventariosede() {
        return iventariosede_idiventariosede;
    }

    public void setIventariosede_idiventariosede(int iventariosede_idiventariosede) {
        this.iventariosede_idiventariosede = iventariosede_idiventariosede;
    }

    private int iventariosede_idiventariosede;

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
