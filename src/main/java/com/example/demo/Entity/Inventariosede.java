package com.example.demo.Entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventariosede")
public class Inventariosede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idiventariosede;

    @Column
    private int stock;

    @Column
    private Date fechallegada;

    @ManyToOne
    @JoinColumn(name = "inventarioproducto_idinventario")
    private Inventarioproducto inventarioproductoidinventario;

    @Column
    private String estado;

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
