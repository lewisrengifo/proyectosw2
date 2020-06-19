package com.example.demo.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idcategoria;

    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Size(max = 45,message = "el texto no puede tener más de 45 caracteres")
    private String nombrecategoria;

    @NotBlank(message = "el campo del texto no puede estar vacio")
    @Pattern(regexp="[a-zA-ZÀ\\u00f1\\u00d1]{1}",message = "Solo acepta una letra y sin tílde")
    private String codigocategoria;

    public int getIdcategoria() {
        return idcategoria;
    }

    public void setIdcategoria(int idcategoria) {
        this.idcategoria = idcategoria;
    }

    public String getNombrecategoria() {
        return nombrecategoria;
    }

    public void setNombrecategoria(String nombrecategoria) {
        this.nombrecategoria = nombrecategoria;
    }

    public String getCodigocategoria() {
        return codigocategoria;
    }

    public void setCodigocategoria(String codigocategoria) {
        this.codigocategoria = codigocategoria;
    }
}
