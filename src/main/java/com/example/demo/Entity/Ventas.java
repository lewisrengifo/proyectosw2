package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "ventas")
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idventas;

    private String rucdni;

    @NotBlank(message = "El nombre no debe ser vacío")
    private String nombrecomprador;

    @Digits(integer = 12, fraction = 0, message = "Se aceptan sólo numeros hasta 12 dígitos.")
    @Min(value=0)
    private String numerodocumento;

    @NotBlank(message = "El nombre no debe ser vacío")
    private String lugarventa;

    @Column(nullable = false)
    @NotNull(message = "la fecha no debe ser nula")
    @Temporal(TemporalType.DATE)
    private Date fechaventa;

    @NotBlank(message = "El nombre no debe ser vacío")
    @Pattern(regexp="[a-zA-ZÀ-ÿ\\u00f1\\u00d1]{1,45}",message = "Solo aceptan letras")
    private String tipodocumento;

    @ManyToOne
    @JoinColumn(name = "sede_idsede")
    private Sede sede;

    @ManyToOne
    @JoinColumn(name = "tienda_idtienda")
    private Tienda tienda;

    @ManyToOne
    @JoinColumn(name = "iventariosede_idiventariosede")
    private Inventariosede inventariosede;

    @NotNull(message = "No puede ser nulo.")
    @Digits(integer = 5, fraction = 0)
    @Min(value=1)
    @Max(value=32767)
    private int cantidad;

    @NotBlank(message = "No debe ser vacío")
    @Pattern(regexp="[a-zA-ZÀ-ÿ\\u00f1\\u00d1]{1,45}",message = "Solo aceptan letras")
    private String metodopago;

    public String getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(String metodopago) {
        this.metodopago = metodopago;
    }



    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public Inventariosede getInventariosede() {
        return inventariosede;
    }

    public void setInventariosede(Inventariosede inventariosede) {
        this.inventariosede = inventariosede;
    }
}