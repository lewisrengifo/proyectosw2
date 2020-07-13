package com.example.demo.Repository;


import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Tienda;
import com.example.demo.Entity.Usuario;
import com.example.demo.Entity.Ventas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Ventas,Integer> {

    @Query(value = "SELECT * FROM ventas where sede_idsede=?1", nativeQuery = true)
    List<Ventas> listaVentasPorSede(int id);

    @Query(value = "select * from ventas where idventas not in (select v.idventas from ventas v where v.idventas=?1);", nativeQuery = true)
    List<Ventas> buscarmenosmio(int idventas);

    
}
