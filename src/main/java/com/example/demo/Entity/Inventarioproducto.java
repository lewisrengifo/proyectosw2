package com.example.demo.Entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inventarioproducto")
public class Inventarioproducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idinventario;

    @Column
    private String numeropedido;

    @ManyToOne
    @JoinColumn(name = "producto_idproducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "categoria_idcategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "tamano_idtamano")
    private Tamano tamano;

    @Column
    private int cantidad;

    @Column
    private String color;

    @Column
    private Double costomosqoy;

    @Column
    private Double costotejedor;

    @Column
    private String facilitador;

    @Column
    private String codigogenerado;

    @ManyToOne
    @JoinColumn(name = "consignacionyventa_idconsignacion")
    private Consignacionyventa consignacionyventa;

    @Column
    private Date fechainicio;

    public int getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(int idinventario) {
        this.idinventario = idinventario;
    }

    public String getNumeropedido() {
        return numeropedido;
    }

    public void setNumeropedido(String numeropedido) {
        this.numeropedido = numeropedido;
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

    public Double getCostomosqoy() {
        return costomosqoy;
    }

    public void setCostomosqoy(Double costomosqoy) {
        this.costomosqoy = costomosqoy;
    }

    public Double getCostotejedor() {
        return costotejedor;
    }

    public void setCostotejedor(Double costotejedor) {
        this.costotejedor = costotejedor;
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
