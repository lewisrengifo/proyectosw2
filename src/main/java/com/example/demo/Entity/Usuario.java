package com.example.demo.Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int idusuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private String correo;
    private String contrasena;
    @Column(nullable = true)
    private String token;
    @ManyToOne
    @JoinColumn(name = "sede_idrol")
    private Sede sede_idrol;
    @Column(nullable = true)
    private boolean enable;
    @ManyToOne
    @JoinColumn(name = "rol_idrol")
    private Rol rol_idrol;




    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }


    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Sede getSede_idrol() {
        return sede_idrol;
    }

    public void setSede_idrol(Sede sede_idrol) {
        this.sede_idrol = sede_idrol;
    }

    public Rol getRol_idrol() {
        return rol_idrol;
    }

    public void setRol_idrol(Rol rol_idrol) {
        this.rol_idrol = rol_idrol;
    }
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


}
