package com.example.demo.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "consignacionyventa")
public class Consignacionyventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idconsignacion;

    @Column
    private Date fechafin;

    @ManyToOne
    @JoinColumn(name = "artesano_idartesano")
    private Artesano artesano;

    @Column
    private String tipo;

    @Column
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

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
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


}
