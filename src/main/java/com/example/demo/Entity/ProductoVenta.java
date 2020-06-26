package com.example.demo.Entity;

import java.math.BigDecimal;

public class ProductoVenta extends Inventarioproducto {

    @Override
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    private int cantidad;

    public ProductoVenta(){

    }

    public ProductoVenta(Producto producto, Categoria categoria, Tamano tamano, String color, BigDecimal preciomosqoy) {
       super(producto, categoria, tamano,color, preciomosqoy);
       this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getTotal() {
        return this.getPreciomosqoy().doubleValue() * this.cantidad;
    }
}
