package com.example.demo.Entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class ProductoVenta extends Inventarioproducto {

    @Override
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    @ManyToOne
    @JoinColumn
    private Ventas ventas;

    private int cantidad;

    public ProductoVenta(){

    }



    public void aumentarCantidad(int cantidad) {
        this.cantidad=cantidad+this.cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getTotal() {
        return this.getPreciomosqoy().doubleValue() * this.cantidad;
    }
}
