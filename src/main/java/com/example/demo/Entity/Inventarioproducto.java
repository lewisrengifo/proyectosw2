package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="inventarioproducto")
public class Inventarioproducto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idinventario;


    @ManyToOne
    @JoinColumn(name = "producto_idproducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "categoria_idcategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "tamano_idtamano")
    private Tamano tamano;

    private int cantidad;
    private String color;
    @Column(name = "costomosqoy")
    private Double preciomosqoy;
    @Column(name = "costotejedor")
    private Double preciotejedor;
    @Column(nullable = false)
    private String facilitador;
    @Column(nullable = false)
    private String codigogenerado;

    @ManyToOne
    @JoinColumn(name = "consignacionyventa_idconsignacion")
    private Consignacionyventa consignacionyventa;

    private Date fechainicio;

    public Inventarioproducto(){

    }

   public Inventarioproducto(Producto producto, Categoria categoria, Tamano tamano, String color, Double preciomosqoy) {
        this.producto= producto;
        this.categoria = categoria;
        this.tamano = tamano;
        this.color = color;
       this.preciomosqoy = preciomosqoy;
   }

    public int getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(int idinventario) {
        this.idinventario = idinventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Tamano getTamano() {
        return tamano;
    }

    public void setTamano(Tamano tamano) {
        this.tamano = tamano;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPreciomosqoy() {
        return preciomosqoy;
    }

    public void setPreciomosqoy(Double preciomosqoy) {
        this.preciomosqoy = preciomosqoy;
    }

    public Double getPreciotejedor() {
        return preciotejedor;
    }

    public void setPreciotejedor(Double preciotejedor) {
        this.preciotejedor = preciotejedor;
    }

    public String getFacilitador() {
        return facilitador;
    }

    public void setFacilitador(String facilitador) {
        this.facilitador = facilitador;
    }

    public String getCodigogenerado() {
        return codigogenerado;
    }

    public void setCodigogenerado(String codigogenerado) {
        this.codigogenerado = codigogenerado;
    }

    public Consignacionyventa getConsignacionyventa() {
        return consignacionyventa;
    }

    public void setConsignacionyventa(Consignacionyventa consignacionyventa) {
        this.consignacionyventa = consignacionyventa;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }
}