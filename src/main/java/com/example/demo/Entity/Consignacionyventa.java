package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "consignacionyventa")
public class Consignacionyventa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idconsignacion;

    @Column
    @NotBlank(message = "La fecha fin no puede estar en blanco")
    private Date fechafin;

    @ManyToOne
    @JoinColumn(name = "artesano_idartesano")
    @NotBlank(message = "El texto no puede estar en blanco")
    private Artesano artesano;

    @Column(nullable = false)
    @NotBlank(message = "El tipo no puede estar en blanco")
    @Pattern(regexp="[a-zA-Z]{1,15}",message = "Solo aceptan letras y un maximo de 15 caracteres")
    private String tipo;

    @Column(nullable = false)
    @NotBlank(message = "La fecha de inicio no puede estar en blanco")
    private Date fechainicio;

    public int getIdconsignacion() {
        return idconsignacion;
    }

    public void setIdconsignacion(int idconsignacion) {
        this.idconsignacion = idconsignacion;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public Artesano getArtesano() {
        return artesano;
    }

    public void setArtesano(Artesano artesano) {
        this.artesano = artesano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }
}
