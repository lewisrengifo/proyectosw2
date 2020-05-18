package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    private int idproducto;

    @Column(name = "nombreproducto")
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Size(max = 45,message = "El nombre del producto no puede tener más de 45 caracteres")
    private String nombreproducto;

    @Column(name = "codigoproducto")
    @NotBlank(message = "El código del producto no puede estar vacío")
    @Size(max = 3,message = "El código del producto no puede tener más de 3 caracteres")
    private String codigoproducto;

    @Column(name = "descripcionproducto")
    @NotBlank(message = "La descripción del producto no puede estar vacio")
    @Size(max = 45,message = "La descripción del producto no puede tener más de 45 caracteres")
    private String descripcionproducto;

    @Column(name = "codigodescripcionproducto")
    @NotBlank(message = "El código de la descripción del producto no puede estar vacio")
    @Size(max = 3,message = "El código de la descripción del producto no puede tener más de 3 caracteres")
    private String codigodescripcionproducto;

    @Column(name = "linea_idlinea")
    @NotBlank(message = "La línea no puede estar vacío")
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
