package com.example.demo.Repository;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
            "  where fechaventa like '%?1%' ;", nativeQuery = true)
    ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoy(String fechaventa);
    //Concatenar el ano-mes

}
