package com.example.demo.Repository;

import com.example.demo.Entity.Consignacionyventa;

import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Ventas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsignacionyventaRepository extends JpaRepository<Consignacionyventa, Integer> {

    @Query(value = "SELECT MAX(idconsignacion) as id FROM consignacionyventa", nativeQuery = true)
    int ultimoConsiyVentaIngresado();

    Consignacionyventa findTopByOrderByIdconsignacionDesc();


    @Query(value = "SELECT cyv.* FROM consignacionyventa cyv " +
            "inner join artesano a on a.idartesano=cyv.artesano_idartesano " +
            "inner join comunidad com on com.idcomunidad=a.comunidad_idcomunidad " +
            "where cyv.numeropedido like %?1% or cyv.tipo like %?1% or cyv.fechainicio like %?1% or cyv.fechafin like %?1% " +
            "or a.nombreartesano like %?1% or a.apellidopaterno like %?1% or com.nombrecomunidad like %?1%",
            countQuery = "SELECT count(*) FROM consignacionyventa cyv " +
                    "inner join artesano a on a.idartesano=cyv.artesano_idartesano " +
                    "inner join comunidad com on com.idcomunidad=a.comunidad_idcomunidad " +
                    "where cyv.numeropedido like %?1% or cyv.tipo like %?1% or cyv.fechainicio like %?1% or cyv.fechafin like %?1% " +
                    "or a.nombreartesano like %?1% or a.apellidopaterno like %?1% or com.nombrecomunidad like %?1%",
            nativeQuery = true)
    Page<Consignacionyventa> buscadorConsignacionesYVentas(String search, Pageable pageable);

    @Query(value="select * from proyectobasesw2.consignacionyventa where artesano_idartesano = ?1 limit 1;",nativeQuery = true)
    Consignacionyventa verificaArtesanoEnConsignacionYVenta(int id);

    @Query(value = "SELECT * FROM consignacionyventa order by idconsignacion desc",nativeQuery = true)
    Page<Consignacionyventa> listaConsigVenta(Pageable pageable);

}


