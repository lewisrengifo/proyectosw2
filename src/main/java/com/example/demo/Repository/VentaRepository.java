package com.example.demo.Repository;

import com.example.demo.Dto.ReporteMensualAnualSedeDto;
import com.example.demo.Dto.UsuarioSedeDto;
import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VentaRepository extends JpaRepository<Ventas,Integer> {


    //Query para el caso de mensual/anual de mosqoy
    //Falta seleccionar los campos que se deben mostrar por reportes, dependerá de la base de datos que tengamos.
    //Crear Dto para el caso particular de sedes, artículos, comunidad, nombre del cliente

    @Query(value = "SELECT nombrecomprador as cliente, numerodocumento as documento, lugarventa as lugar, tipodocumento as tipodocumento, fechaventa as fechaventa  FROM proyectobasesw2.ventas\n" +
            "where fechaventa like '%?1%';", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoy(String fechaventa);



    //Query para el reporte mensual/anual por ventas en sede
    //Select modificar de acuerdo a lo que se tiene en la base de datos

    @Query(value = "s.nombre as nombresede, v.nombrecomprador as cliente, v.numerodocumento as documento, v.lugarventa as lugar, v.tipodocumento as tipodocumento, v.fechaventa as fecha FROM ventas v inner join sede s on s.idsede = v.idsede \n" +
            "where v.fechaventa like %?1% and (s.sede_idrol like %?2% or  s.nombre like %?2%);", nativeQuery = true)
    ReporteMensualAnualSedeDto reporteMensualAnualSede(String fechaventa, String sede);


    //Query para el reporte mensual/anual por ventas por cliente
    @Query(value = "SELECT nombrecomprador as cliente, numerodocumento as documento, lugarventa as lugar, tipodocumento as tipodocumento, fechaventa as fechaventa  FROM proyectobasesw2.ventas \n" +
            "where fechaventa like '%?1%' and nombrecomprador = ?2;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteCliente(String fechaventa, String cliente);


    //mensual/anual para comunidad
    @Query(value = "SELECT v.nombrecomprador as cliente, v.numerodocumento as documento, v.lugarventa as lugar, v.tipodocumento as tipodocumento, v.fechaventa as fechaventa  FROM ventas v inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede\n" +
            "inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario\n" +
            "inner join consignacionyventa consve on consve.idconsignacion = invp.consignacionyventa_idconsignacion\n" +
            "inner join artesano art on art.idartesano = consve.artesano_idartesano\n" +
            "inner join comunidad com on com.idcomunidad = art.comunidad_idcomunidad\n" +
            "where v.fechaventa like %?1% and com.nombrecomunidad = ?2;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteComunidad(String fechaventa, String comunidad);


}
