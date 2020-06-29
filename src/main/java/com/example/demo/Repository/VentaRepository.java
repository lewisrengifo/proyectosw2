package com.example.demo.Repository;

import com.example.demo.Entity.Usuario;
import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface VentaRepository extends JpaRepository<Ventas,Integer> {

    @Query(value = "SELECT * FROM ventas where sede_idrol=?1", nativeQuery = true)
    List<Ventas> listaVentasPorSede(int id);
}
