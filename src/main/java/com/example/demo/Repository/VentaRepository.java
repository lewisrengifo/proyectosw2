package com.example.demo.Repository;




import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;

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

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Ventas, Integer> {

    @Query(value = "SELECT * FROM ventas where sede_idsede=?1 order by idventas desc",
            countQuery = "SELECT count(*) FROM ventas where sede_idsede= ?1 ",

            nativeQuery = true)
    Page<Ventas> listaVentasPorSedePageable(int id, Pageable pageable);



    //Query para el caso de mensual/anual de mosqoy
    //Falta seleccionar los campos que se deben mostrar por reportes, dependerá de la base de datos que tengamos.
    //Crear Dto para el caso particular de sedes, artículos, comunidad, nombre del cliente

    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like '%?1%' ;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoy(String fechaventa);


    //Query para el reporte mensual/anual por ventas en sede
    //Select modificar de acuerdo a lo que se tiene en la base de datos

    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like %?1% and v.sede_idsede = ?2;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteMensualAnualSede(String fechaventa, String sede);


    //Query para el reporte mensual/anual por ventas por cliente
    @Query(value = " SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like %?1% and v.lugarventa = ?2;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteCliente(String fechaventa, String cliente);


    //mensual/anual para comunidad
    @Query(value = "SELECT v.nombrecomprador as cliente, v.numerodocumento as documento, v.lugarventa as lugar, v.tipodocumento as tipodocumento, v.fechaventa as fechaventa  FROM ventas v inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede\n" +
            "inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario\n" +
            "inner join consignacionyventa consve on consve.idconsignacion = invp.consignacionyventa_idconsignacion\n" +
            "inner join artesano art on art.idartesano = consve.artesano_idartesano\n" +
            "inner join comunidad com on com.idcomunidad = art.comunidad_idcomunidad\n" +
            "where v.fechaventa like %?1% and com.nombrecomunidad = ?2;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteComunidad(String fechaventa, String comunidad);


    @Query(value = "SELECT * FROM ventas where sede_idsede=?1", nativeQuery = true)
    List<Ventas> listaVentasPorSede(int id);


    @Query(value = "select * from ventas where idventas not in (select v.idventas from ventas v where v.idventas=?1);", nativeQuery = true)
    List<Ventas> buscarmenosmio(int idventas);

    @Query(value = "SELECT v.* FROM ventas v " +
            "inner join tienda t on t.idtienda=v.tienda_idtienda " +
            "inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede " +
            " inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario " +
            "inner join producto p on p.idproducto = invp.producto_idproducto " +
            "inner join sede s on s.idsede = v.sede_idsede " +
            "where (t.nombre like %?1% or v.fechaventa like %?1% or v.tipodocumento like %?1% " +
            "or v.numerodocumento like %?1% or v.rucdni like %?1% or v.nombrecomprador like %?1% " +
            "or v.cantidad like %?1% or invp.codigogenerado like %?1% or p.nombreproducto like %?1% " +
            "or invp.color like %?1% or v.metodopago like %?1%) and s.nombre=?2",

            countQuery = " SELECT count(*) FROM ventas v inner join tienda t on t.idtienda=v.tienda_idtienda inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario inner join producto p on p.idproducto = invp.producto_idproducto inner join sede s on s.idsede =  v.sede_idsede where (t.nombre like %?1% or v.fechaventa like %?1% or v.tipodocumento like %?1% or v.numerodocumento like %?1% or v.rucdni like %?1% or v.nombrecomprador like %?1% or v.cantidad like %?1% or invp.codigogenerado like %?1% or p.nombreproducto like %?1% or invp.color like %?1% or v.metodopago like %?1%) and s.nombre=?2",
            nativeQuery = true)
    Page<Ventas> buscadorVentas(String search, String nombreSede, Pageable pageable);

}
