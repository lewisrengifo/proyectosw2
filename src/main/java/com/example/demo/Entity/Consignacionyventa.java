package com.example.demo.Entity;


import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "consignacionyventa")
public class Consignacionyventa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idconsignacion;

    private String numeropedido;

    @ManyToOne
    @JoinColumn(name = "artesano_idartesano")
    private Artesano artesano;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private Date fechainicio;

    private Date fechafin;

    public String getNumeropedido() {
        return numeropedido;
    }

    public void setNumeropedido(String numeropedido) {
        this.numeropedido = numeropedido;
    }

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
