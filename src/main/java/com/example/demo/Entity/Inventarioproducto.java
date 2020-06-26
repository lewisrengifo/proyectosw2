package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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

    @Pattern(regexp = "[0-9]{4}" ,message = "Solo se aceptan números")
    private int cantidad;
    @Pattern(regexp="[a-zA-ZÀ-ÿ\\u00f1\\u00d1]{1,45}",message = "Solo se aceptan letras")
    private String color;

    @Digits(integer = 6, fraction = 2)
    @Min(value = 1, message = "Solo se permiten números positivos")
    @Column(name = "costomosqoy")
    private BigDecimal preciomosqoy;

    @Digits(integer = 6, fraction = 2)
    @Min(value = 1, message = "Solo se permiten números positivos")
    @Column(name = "costotejedor")
    private BigDecimal preciotejedor;

    @Column(nullable = false)
    @Pattern(regexp="[a-zA-ZÀ-ÿ\\u00f1\\u00d1]{1,45}",message = "Solo se aceptan letras")
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

    public BigDecimal getPreciomosqoy() {return preciomosqoy;}

    public void setPreciomosqoy(BigDecimal preciomosqoy) {this.preciomosqoy = preciomosqoy;}

    public BigDecimal getPreciotejedor() {return preciotejedor;}

    public void setPreciotejedor(BigDecimal preciotejedor) {this.preciotejedor = preciotejedor;}

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