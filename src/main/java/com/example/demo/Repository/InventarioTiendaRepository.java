package com.example.demo.Repository;


import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda, Integer> {
    @Query(value = "SELECT invt.* FROM inventariotienda invt", nativeQuery = true)
    Page<Inventariotienda> getevertienda(Pageable pageable);


    @Query(value = "SELECT invt.* FROM inventariotienda invt inner join tienda t on t.idtienda=invt.tienda_idtienda " +
            "inner join inventariosede inS on inS.idiventariosede=invt.iventariosede_idiventariosede " +
            "inner join sede s on s.idsede=t.sede_idsede " +
            "inner join inventarioproducto inP on inP.idinventario=inS.inventarioproducto_idinventario " +
            "inner join producto pr on pr.idproducto = inP.producto_idproducto " +
            "where (t.nombre like %?1% or invt.stocktienda like %?1% or inP.codigogenerado like %?1% " +
            "or invt.fechaentrega like %?1% or invt.estado like %?1% or pr.nombreproducto like %?1%) " +
            "and s.nombre = ?2 and invt.estado='recibido'",
            countQuery = "SELECT count(*) FROM inventariotienda invt inner join tienda t on t.idtienda=invt.tienda_idtienda " +
                    "inner join inventariosede inS on inS.idiventariosede=invt.iventariosede_idiventariosede " +
                    "inner join sede s on s.idsede=t.sede_idsede " +
                    "inner join inventarioproducto inP on inP.idinventario=inS.inventarioproducto_idinventario " +
                    "inner join producto pr on pr.idproducto = inP.producto_idproducto " +
                    "where (t.nombre like %?1% or invt.stocktienda like %?1% or inP.codigogenerado like %?1% " +
                    "or invt.fechaentrega like %?1% or invt.estado like %?1% or pr.nombreproducto like %?1%) " +
                    "and s.nombre = ?2 and invt.estado='recibido'",
            nativeQuery = true)
    Page<Inventariotienda> buscadorInventarioTienda(String search, String nombresede, Pageable pageable);

    @Query(value = "SELECT invt.* FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p, " +
            "tienda ti, sede se where (p.nombreproducto like %?1% or ti.nombre like %?1% or inve.codigogenerado like %?1% or " +
            "invt.estado like %?1% or invt.stocktienda like %?1% or se.nombre like %?1% ) and invt.estado='recibido'  and p.idproducto=inve.producto_idproducto " +
            "and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede " +
            "and ti.idtienda = invt.tienda_idtienda and ti.sede_idsede=se.idsede",
            countQuery = "SELECT count(*) FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p, tienda ti, sede se where (p.nombreproducto like %?1% or ti.nombre like %?1% or inve.codigogenerado like %?1% or invt.estado like %?1% or invt.stocktienda like %?1% or se.nombre like %?1%) and invt.estado='recibido'  and p.idproducto=inve.producto_idproducto and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede and ti.idtienda = invt.tienda_idtienda and ti.sede_idsede=se.idsede"
            , nativeQuery = true)
    Page<Inventariotienda> buscadorInventarioTiendaTotal(String search, Pageable pageable);

    @Query(value = "SELECT it.* FROM inventariotienda it " +
            "inner join tienda t on t.idtienda=it.tienda_idtienda " +
            "where t.sede_idsede = ?1 and estado = 'recibido' order by idiventariotienda desc",
            countQuery = "SELECT count(*) FROM inventariotienda it " +
                    "inner join tienda t on t.idtienda=it.tienda_idtienda " +
                    "where t.sede_idsede = ?1 and estado = 'recibido' ", nativeQuery = true)
    Page<Inventariotienda> listarTiendasEnSede(int idsede, Pageable pageable);

    @Query(value = "SELECT it.* FROM inventariotienda it " +
            "inner join tienda t on t.idtienda=it.tienda_idtienda " +
            "where t.idtienda=?1 and it.estado = 'recibido'", nativeQuery = true)
    List<Inventariotienda> listaProductoEnTienda(int idTienda);

    @Query(value = "SELECT * FROM inventariotienda where tienda_idtienda=?1 and iventariosede_idiventariosede=?2", nativeQuery = true)
    Inventariotienda productoEnTienda(int idTienda, int idSedeProducto);

    @Transactional
    @Modifying
    @Query(value = "UPDATE inventariotienda SET stocktienda = :stocktienda,estado = 'recibido' WHERE (idiventariotienda = :idiventariotienda);", nativeQuery = true)
    void ActualizarCantidadInventarioTienda(@Param("stocktienda") int stocktienda, @Param("idiventariotienda") int idiventariotienda);

    @Transactional
    @Modifying
    @Query(value = "UPDATE inventariotienda SET stocktienda = :cantidad,estado = :estado WHERE (idiventariotienda = :idiventariotienda);", nativeQuery = true)
    void DevolverProductoASede(@Param("cantidad") int cantidadNew, @Param("estado") String devuelto, @Param("idiventariotienda") int idiventariotienda);

    @Query(value = "SELECT invT.* FROM inventariotienda invT where iventariosede_idiventariosede = ?1 and estado = 'recibido' limit 1",nativeQuery = true)
    Inventariotienda productoEntiendaTodavia(int idInventarioSede);

    @Query(value = "SELECT * FROM inventariotienda where estado = 'recibido' order by idiventariotienda desc", nativeQuery = true)
    Page<Inventariotienda> findmenosDevuelto(Pageable pageable);



}
