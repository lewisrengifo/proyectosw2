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
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda,Integer> {


    @Query(value="SELECT invt.* FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p where p.nombreproducto like %?1% \n" +
            "and p.idproducto=inve.producto_idproducto and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede ",
            countQuery ="SELECT count(*) FROM inventariotienda  invt , inventarioproducto inve, inventariosede invs ,producto p where p.nombreproducto like %?1% \n" +
                    "and p.idproducto=inve.producto_idproducto and invs.inventarioproducto_idinventario= inve.idinventario and invs.idiventariosede = invt.iventariosede_idiventariosede",
            nativeQuery = true)
    Page<Inventariotienda> buscadorInventarioTienda(String search, Pageable pageable);

    @Query(value="SELECT it.* FROM inventariotienda it " +
            "inner join tienda t on t.idtienda=it.tienda_idtienda " +
            "where t.sede_idsede = ?1 and estado = 'recibido'",
            countQuery ="SELECT count(*) FROM inventariotienda it " +
            "inner join tienda t on t.idtienda=it.tienda_idtienda " +
            "where t.sede_idsede = ?1 and estado = 'recibido'",nativeQuery=true)
    Page<Inventariotienda> listarTiendasEnSede(int idsede,Pageable pageable);

    @Query(value="SELECT it.* FROM inventariotienda it " +
            "inner join tienda t on t.idtienda=it.tienda_idtienda " +
            "where t.idtienda=?1 and it.estado = 'recibido'",nativeQuery=true)
    List<Inventariotienda> listaProductoEnTienda(int idTienda);

    @Query(value = "SELECT * FROM inventariotienda where tienda_idtienda=?1 and iventariosede_idiventariosede=?2",nativeQuery = true)
    Inventariotienda productoEnTienda(int idTienda , int idSedeProducto);

    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariotienda SET stocktienda = :stocktienda,estado = 'recibido' WHERE (idiventariotienda = :idiventariotienda);", nativeQuery = true)
    void ActualizarCantidadInventarioTienda(@Param("stocktienda") int stocktienda, @Param("idiventariotienda") int idiventariotienda);

    @Transactional
    @Modifying
    @Query(value= "UPDATE inventariotienda SET stocktienda = :cantidad,estado = :estado WHERE (idiventariotienda = :idiventariotienda);", nativeQuery = true)
    void DevolverProductoASede(@Param("cantidad") int cantidadNew,@Param("estado")  String devuelto, @Param("idiventariotienda")int idiventariotienda);


}
