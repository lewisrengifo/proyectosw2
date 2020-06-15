package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "notificaciones")
public class Notificaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idnotificaciones;

    @Column
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "usuario_idUsuario")
    private Usuario usuario;

    public int getIdnotificaciones() {
        return idnotificaciones;
    }

    public void setIdnotificaciones(int idnotificaciones) {
        this.idnotificaciones = idnotificaciones;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
