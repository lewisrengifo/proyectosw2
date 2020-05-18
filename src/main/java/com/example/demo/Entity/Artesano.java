package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="artesano")
public class Artesano  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idartesano;
    @Column(nullable = false)
    @NotBlank(message = "El texto no puede estar vacio")
    @Size(max = 45, message = "el nombre no puede poseer más de 45 caracteres")
    private String nombreartesano;
    @Column(nullable = false)
    @NotBlank(message = "El texto no puede estar vacio")
    @Size(max = 45, message = "el apellido paterno no puede poseer más de 45 caracteres")
    private String apellidopaterno;
    @Size(max = 45, message = "el apellido materno no puede poseer más de 45 caracteres")
    private String apellidomaterno;
    @Column(nullable = false)
    @NotBlank(message = "El texto no puede estar vacio")
    @Size(min=2,max = 4, message = "el codigo no puede ser menor a 2 caracteres ni mayor a 4 caracteres")
    private String codigoartesano;


     @ManyToOne
     @JoinColumn(name = "comunidad_idcomunidad")
    private Comunidad comunidad;

    public Comunidad getComunidad() {
        return comunidad;
    }

    public void setComunidad(Comunidad comunidad) {
        this.comunidad = comunidad;
    }

    public int getIdartesano() {
        return idartesano;
    }

    public void setIdartesano(int idartesano) {
        this.idartesano = idartesano;
    }

    public String getNombreartesano() {
        return nombreartesano;
    }

    public void setNombreartesano(String nombreartesano) {
        this.nombreartesano = nombreartesano;
    }

    public String getApellidopaterno() {
        return apellidopaterno;
    }

    public void setApellidopaterno(String apellidopaterno) {
        this.apellidopaterno = apellidopaterno;
    }

    public String getApellidomaterno() {
        return apellidomaterno;
    }

    public void setApellidomaterno(String apellidomaterno) {
        this.apellidomaterno = apellidomaterno;
    }

    public String getCodigoartesano() {
        return codigoartesano;
    }

    public void setCodigoartesano(String codigoartesano) {
        this.codigoartesano = codigoartesano;
    }


}
