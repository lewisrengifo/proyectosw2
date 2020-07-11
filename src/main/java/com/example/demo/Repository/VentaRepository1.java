package com.example.demo.Repository;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;
import java.util.List;

@Repository
public interface VentaRepository1 extends JpaRepository<Ventas,Integer> {
    //Query para el caso de mensual/anual de mosqoy

    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like %?1% ;", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteAnualMosqoy(String ano);

    //mosqoy trimestre
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where (fechaventa like %:mes1% or fechaventa like %:mes2% or fechaventa like %:mes3%) and fechaventa like %:anotri%", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteTrimestralAnualMosqoy(@Param("mes1") String mes1,@Param("mes2") String mes2, @Param("mes3")String mes3, @Param("anotri")String anotri);


    //SEDE anual y mensual
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like %?1% and v.sede_idsede = ?2 ;", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteMensualAnualSede(String fechaventa, Integer sede);
    //trimestre
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where (fechaventa like %:mes1% or fechaventa like %:mes2% or fechaventa like %:mes3%) and fechaventa like %:anotri% and v.sede_idsede = :sedeid", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteTrimestralSede(@Param("mes1") String mes1,@Param("mes2") String mes2, @Param("mes3")String mes3, @Param("anotri")String anotri, @Param("sedeid") Integer sedeid);

    //mensual/anual para COMUNIDAD
    @Query(value = "SELECT v.nombrecomprador as cliente, v.numerodocumento as documento, v.lugarventa as lugar, v.tipodocumento as tipodocumento, v.fechaventa as fechaventa  FROM ventas v inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede\n" +
            "inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario\n" +
            "inner join consignacionyventa consve on consve.idconsignacion = invp.consignacionyventa_idconsignacion\n" +
            "inner join artesano art on art.idartesano = consve.artesano_idartesano\n" +
            "inner join comunidad com on com.idcomunidad = art.comunidad_idcomunidad\n" +
            "where v.fechaventa like %?1% and com.idcomunidad = ?2;", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteComunidad(String fechaventa, Integer comunidad);
    //trimestre
    @Query(value = "SELECT v.nombrecomprador as cliente, v.numerodocumento as documento, v.lugarventa as lugar, v.tipodocumento as tipodocumento, v.fechaventa as fechaventa  FROM ventas v inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede\n" +
            "inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario\n" +
            "inner join consignacionyventa consve on consve.idconsignacion = invp.consignacionyventa_idconsignacion\n" +
            "inner join artesano art on art.idartesano = consve.artesano_idartesano\n" +
            "inner join comunidad com on com.idcomunidad = art.comunidad_idcomunidad\n" +
            "where (v.fechaventa like %:mes1% or v.fechaventa like %:mes2% or v.fechaventa like %:mes3%) and v.fechaventa like %:anotri% and com.idcomunidad = :comunidadid", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteTrimestreComunidad(@Param("mes1") String mes1,@Param("mes2") String mes2, @Param("mes3")String mes3, @Param("anotri")String anotri, @Param("comunidadid") Integer comunidadid);

    //mensual/anual para CLIENTE
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where fechaventa like %?1% and v.nombrecomprador like %?2% ;", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteCliente(String fechaventa, String nombrecomprador);
    //trimestre
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where (v.fechaventa like %:mes1% or v.fechaventa like %:mes2% or v.fechaventa like %:mes3%) and v.fechaventa like %:anotri% and v.nombrecomprador = :nombrecomprador", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteTrimestreCliente(@Param("mes1") String mes1,@Param("mes2") String mes2, @Param("mes3")String mes3, @Param("anotri")String anotri, @Param("nombrecomprador") String nombrecomprador);
    //mensual/anual para PRODUCTO
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where v.fechaventa like %?1% and p.nombreproducto like %?2%; ", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteProducto(String fechaventa, String nombreproducto);
    @Query(value = "SELECT  v.fechaventa as fechadeventa, v.tipodocumento as tipodedocumento, v.numerodocumento as documento, v.rucdni as rucodni,\n" +
            " v.nombrecomprador as cliente, v.cantidad , p.codigoproducto as codigoproducto, p.nombreproducto as nombredeproducto,\n" +
            " invpr.color as color , v.metodopago as metododepago\n" +
            "  FROM proyectobasesw2.ventas v\n" +
            "  inner join inventariosede invse on invse.idiventariosede = v.iventariosede_idiventariosede\n" +
            "  inner join inventarioproducto invpr on invpr.idinventario = invse.inventarioproducto_idinventario\n" +
            "  inner join producto p on p.idproducto = invpr.producto_idproducto\n" +
            "  where (v.fechaventa like %:mes1% or v.fechaventa like %:mes2% or v.fechaventa like %:mes3%) and v.fechaventa like %:anotri% and p.nombreproducto like %:nombreproducto%", nativeQuery = true)
    List<ReporteMensualoAnualMosqoyDto> reporteTrimestreProducto(@Param("mes1") String mes1,@Param("mes2") String mes2, @Param("mes3")String mes3, @Param("anotri")String anotri, @Param("nombreproducto") String nombreproducto);
}
