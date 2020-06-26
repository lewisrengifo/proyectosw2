package com.example.demo.Entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idventas;

    @Column
    private String rucdni;

    @Column
    private String nombrecomprador;

    @Column
    private String numerodocumento;

    @Column
    private String lugarventa;

    @Column
    private Date fechaventa;

    @Column
    private String tipodocumento;

    @ManyToOne
    @JoinColumn(name = "sede_idrol")
    private Sede sede;

    @ManyToOne
    @JoinColumn(name = "tienda_idtienda")
    private Tienda tienda;

    @OneToMany(mappedBy = "ventas", cascade = CascadeType.ALL)
    private Set<ProductoVenta> productoVenta;
    //@ManyToOne
    //@JoinColumn(name = "iventariosede_idiventariosede")
    //private Inventariosede inventariosede;

    public Set<ProductoVenta> getProductoVenta() {
        return productoVenta;
    }

    public void setProductoVenta(Set<ProductoVenta> productoVenta) {
        this.productoVenta = productoVenta;
    }


    public int getIdventas() {
        return idventas;
    }

    public void setIdventas(int idventas) {
        this.idventas = idventas;
    }

    public String getRucdni() {
        return rucdni;
    }

    public void setRucdni(String rucdni) {
        this.rucdni = rucdni;
    }

    public String getNombrecomprador() {
        return nombrecomprador;
    }

    public void setNombrecomprador(String nombrecomprador) {
        this.nombrecomprador = nombrecomprador;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getLugarventa() {
        return lugarventa;
    }

    public void setLugarventa(String lugarventa) {
        this.lugarventa = lugarventa;
    }

    public Date getFechaventa() {
        return fechaventa;
    }

    public void setFechaventa(Date fechaventa) {
        this.fechaventa = fechaventa;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    //public Inventariosede getInventariosede() {
      //  return inventariosede;
    //}

    //public void setInventariosede(Inventariosede inventariosede) { this.inventariosede = inventariosede;    }
}
