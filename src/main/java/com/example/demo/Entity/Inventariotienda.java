package com.example.demo.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventariotienda")
public class Inventariotienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idiventariotienda;

    @Column
    private int stocktienda;

    @ManyToOne
    @JoinColumn(name = "tienda_idtienda")
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "iventariosede_idiventariosede")
    private Inventariosede inventariosede;

    @Column
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
