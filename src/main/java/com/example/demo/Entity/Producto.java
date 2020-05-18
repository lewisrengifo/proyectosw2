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
}
