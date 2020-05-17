package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table
public class Usuario {
    @Id
    @Column(name = "idUsuario")
    private int id_usuario;
    private String nombre;
    private String correo;
    private String contraseña;
    @Column(nullable = true)
    private String token;
    @ManyToOne
    @JoinColumn(name = "idrol")
    private Sede sede_idrol;
    @Column(nullable = true)
    private String enable;
    @ManyToOne
    @JoinColumn(name = "idrol")
    private Rol rol_idrol;

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Rol getRol_idrol() {
        return rol_idrol;
    }

    public void setRol_idrol(Rol rol_idrol) {
        this.rol_idrol = rol_idrol;
    }
}
