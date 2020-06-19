package com.example.demo.Entity;

import sun.util.calendar.BaseCalendar;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.util.Date;

@Entity
@Table(name = "consignacionyventa")
public class Consignacionyventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idconsignacion;
    private Date fechafin;

    @ManyToOne
    @JoinColumn(name = "artesano_idartesano")
    private Artesano artesano;

    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private Date fechainicio;

    public int getIdconsignacion() {
        return idconsignacion;
    }

    public void setIdconsignacion(int idconsignacion) {
        this.idconsignacion = idconsignacion;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Artesano getArtesano() {
        return artesano;
    }

    public void setArtesano(Artesano artesano) {
        this.artesano = artesano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }
}
