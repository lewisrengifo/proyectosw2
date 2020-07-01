package com.example.demo.Repository;


import com.example.demo.Dto.ReporteMensualAnualSedeDto;
import com.example.demo.Dto.UsuarioSedeDto;
import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Ventas,Integer> {



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
            "  where fechaventa like '%2020%' ;", nativeQuery = true)
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


}
