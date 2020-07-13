package com.example.demo.Repository;


import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Tienda;
import com.example.demo.Entity.Usuario;
import com.example.demo.Entity.Ventas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Ventas,Integer> {

    @Query(value = "SELECT * FROM ventas where sede_idsede=?1", nativeQuery = true)
    List<Ventas> listaVentasPorSede(int id);

    @Query(value = "select * from ventas where idventas not in (select v.idventas from ventas v where v.idventas=?1);", nativeQuery = true)
    List<Ventas> buscarmenosmio(int idventas);

    @Query(value="SELECT v.* FROM ventas v " +
            "inner join tienda t on t.idtienda=v.tienda_idtienda " +
            "inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede " +
            " inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario " +
            "inner join producto p on p.idproducto = invp.producto_idproducto " +
            "inner join sede s on s.idsede = v.sede_idsede " +
            "where (t.nombre like %?1% or v.fechaventa like %?1% or v.tipodocumento like %?1% " +
            "or v.numerodocumento like %?1% or v.rucdni like %?1% or v.nombrecomprador like %?1% " +
            "or v.cantidad like %?1% or invp.codigogenerado like %?1% or p.nombreproducto like %?1% " +
            "or invp.color like %?1% or v.metodopago like %?1%) and s.nombre=?2",
            countQuery =" SELECT count(*) FROM ventas v inner join tienda t on t.idtienda=v.tienda_idtienda inner join inventariosede invs on invs.idiventariosede = v.iventariosede_idiventariosede inner join inventarioproducto invp on invp.idinventario = invs.inventarioproducto_idinventario inner join producto p on p.idproducto = invp.producto_idproducto inner join sede s on s.idsede =  v.sede_idsede where (t.nombre like %?1% or v.fechaventa like %?1% or v.tipodocumento like %?1% or v.numerodocumento like %?1% or v.rucdni like %?1% or v.nombrecomprador like %?1% or v.cantidad like %?1% or invp.codigogenerado like %?1% or p.nombreproducto like %?1% or invp.color like %?1% or v.metodopago like %?1%) and s.nombre=?2",
            nativeQuery = true)
    Page<Ventas> buscadorVentas(String search,String nombreSede, Pageable pageable);
}
