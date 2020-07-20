package com.example.demo.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "inventariotienda")
public class Inventariotienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idiventariotienda;

    @Column
    @NotNull(message = "No puede ser nulo")
    @Min(value = 1, message = "El valor minimo debe ser 1")
    private int stocktienda;

    @ManyToOne
    @JoinColumn(name = "tienda_idtienda")
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "iventariosede_idiventariosede")
    private Inventariosede inventariosede;
    @NotNull(message = "la fecha no puede ser nula")
    @Column
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date fechaentrega;

    @Column
    private String estado;

    public int getIdiventariotienda() {
        return idiventariotienda;
    }

    public void setIdiventariotienda(int idiventariotienda) {
        this.idiventariotienda = idiventariotienda;
    }

    public int getStocktienda() {
        return stocktienda;
    }

    public void setStocktienda(int stocktienda) {
        this.stocktienda = stocktienda;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public Inventariosede getInventariosede() {
        return inventariosede;
    }

    public void setInventariosede(Inventariosede inventariosede) {
        this.inventariosede = inventariosede;
    }

    public Date getFechaentrega() {
        return fechaentrega;
    }

    public void setFechaentrega(Date fechaentrega) {
        this.fechaentrega = fechaentrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
