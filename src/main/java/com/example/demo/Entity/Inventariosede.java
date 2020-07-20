package com.example.demo.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "inventariosede")
public class Inventariosede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idiventariosede;

    @Column
    @NotNull(message = "no puede ser vacio")
    @Min(value = 1, message = "El valor minimo es 1")
    private int stock;

    @Column
    @NotNull(message = "La fecha no debe ser nula")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date fechallegada;

    @ManyToOne
    @JoinColumn(name = "inventarioproducto_idinventario")
    private Inventarioproducto inventarioproductoidinventario;

    @Column
    private String estado;

    @ManyToOne
    @JoinColumn(name = "sede_idsede")
    private Sede sede;

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column
    private String observaciones;

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public int getIdiventariosede() {
        return idiventariosede;
    }

    public void setIdiventariosede(int idiventariosede) {
        this.idiventariosede = idiventariosede;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getFechallegada() {
        return fechallegada;
    }

    public void setFechallegada(Date fechallegada) {
        this.fechallegada = fechallegada;
    }

    public Inventarioproducto getInventarioproductoidinventario() {
        return inventarioproductoidinventario;
    }

    public void setInventarioproductoidinventario(Inventarioproducto inventarioproductoidinventario) {
        this.inventarioproductoidinventario = inventarioproductoidinventario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
