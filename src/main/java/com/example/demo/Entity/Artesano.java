package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="artesano")
public class Artesano  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int idartesano;

    @NotBlank(message = "El texto no puede estar vacio")
    //@Size(max = 45, message = "el nombre NO puede poseer más de 45 caracteres")
    @Pattern(regexp = "[a-zA-Z\\u00f1\\u00d1]{1,45}",message = "solo se debe ingresar letras y un maximo de 45 caracteres")
    private String nombreartesano;


    @NotBlank(message = "El texto NO puede estar vacio")
    //@Size(max = 45, message = "el apellido paterno NO puede poseer más de 45 caracteres")
    @Pattern(regexp = "[a-zA-Z\\u00f1\\u00d1]{1,45}",message = "solo se debe ingresar letras y un máximo de 45 caracteres")
    private String apellidopaterno;

    @Size(max = 45, message = "el apellido materno NO puede poseer más de 45 caracteres")
    //@Pattern(regexp = "[a-zA-Z]{1,45}",message = "solo se debe ingresar letras")
    private String apellidomaterno;


    @NotBlank(message = "El texto NO puede estar vacio")
    //@Size(min=2,max = 4, message = "el codigo debe tener como mínimo 2 caracteres y máximo 4 caracteres")
    @Pattern(regexp = "[a-zA-Z\\u00f1\\u00d1]{2,4}",message = "solo se debe ingresar letras, minimo 2 y maximo 4 caracteres")
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
