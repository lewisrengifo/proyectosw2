package com.example.demo.Entity;


import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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


    public ProductoVenta(Producto producto, Categoria categoria, Tamano tamano, String color, Double preciomosqoy, int cantidad) {

       super(producto, categoria, tamano,color, preciomosqoy);
       this.cantidad = cantidad;
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
