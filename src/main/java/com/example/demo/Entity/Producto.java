package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    private int idproducto;

    @Column(name = "nombreproducto")
    private String nombreproducto;

    @Column(name = "codigoproducto")
    private String codigoproducto;

    @Column(name = "descripcionproducto")
    private String descripcionproducto;

    @Column(name = "codigodescripcionproducto")
    private String codigodescripcionproducto;

    @Column(name = "linea_idlinea")
    private int linea;

    @Column(name = "foto")
    private String foto;

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public String getDescripcionproducto() {
        return descripcionproducto;
    }

    public void setDescripcionproducto(String descripcionproducto) {
        this.descripcionproducto = descripcionproducto;
    }

    public String getCodigodescripcionproducto() {
        return codigodescripcionproducto;
    }

    public void setCodigodescripcionproducto(String codigodescripcionproducto) {
        this.codigodescripcionproducto = codigodescripcionproducto;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
