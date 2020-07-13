package com.example.demo.Repository;

import antlr.collections.List;
import com.example.demo.Entity.Consignacionyventa;
import com.example.demo.Entity.Inventarioproducto;

import com.example.demo.Entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.Entity.Producto;
import com.example.demo.Entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface InventarioproductoRepository extends JpaRepository <Inventarioproducto, Integer> {



    @Query(value="SELECT * FROM inventarioproducto invp\n" +
            "inner join producto p on p.idproducto=invp.producto_idproducto " +
            "inner join linea l on l.idlinea=p.linea_idlinea\n" +
            "inner join categoria cat  on cat.idcategoria=invp.categoria_idcategoria \n" +
            "inner join tamano t on t.idtamano=invp.tamano_idtamano\n" +
            "inner join consignacionyventa cyv on cyv.idconsignacion=invp.consignacionyventa_idconsignacion\n" +
            "inner join artesano a on a.idartesano = cyv.artesano_idartesano\n" +
            "inner join comunidad com on com.idcomunidad=a.comunidad_idcomunidad\n" +
            "where com.nombrecomunidad like %?1%\n" +
            "or a.nombreartesano like %?1%\n" +
            "or l.nombrelinea like %?1%\n" +
            "or p.nombreproducto like %?1%\n" +
            "or p.descripcionproducto like %?1%\n" +
            "or cat.nombrecategoria like %?1%\n" +
            "or t.nombretamano like %?1%\n" +
            "or invp.cantidad like %?1%\n" +
            "or invp.color like %?1%\n" +
            "or invp.costomosqoy like %?1%\n" +
            "or invp.costotejedor like %?1%\n" +
            "or invp.facilitador like %?1% \n" +
            "or invp.codigogenerado like %?1%\n" +
            "or cyv.fechainicio like %?1% \n" +
            "or cyv.numeropedido like %?1% ",
            countQuery =" SELECT count(*) FROM inventarioproducto invp\n" +
                    "inner join producto p on p.idproducto=invp.producto_idproducto " +
                    "inner join linea l on l.idlinea=p.linea_idlinea\n" +
                    "inner join categoria cat on cat.idcategoria=invp.categoria_idcategoria \n" +
                    "inner join tamano t on t.idtamano=invp.tamano_idtamano\n" +
                    "inner join consignacionyventa cyv on cyv.idconsignacion=invp.consignacionyventa_idconsignacion\n" +
                    "inner join artesano a on a.idartesano = cyv.artesano_idartesano\n" +
                    "inner join comunidad com on com.idcomunidad=a.comunidad_idcomunidad\n" +
                    "where com.nombrecomunidad like %?1%\n" +
                    "or a.nombreartesano like %?1%\n" +
                    "or l.nombrelinea like %?1%\n" +
                    "or p.nombreproducto like %?1%\n" +
                    "or p.descripcionproducto like %?1%\n" +
                    "or cat.nombrecategoria like %?1%\n" +
                    "or t.nombretamano like %?1%\n" +
                    "or invp.cantidad like %?1%\n" +
                    "or invp.color like %?1%\n" +
                    "or invp.costomosqoy like %?1%\n" +
                    "or invp.costotejedor like %?1%\n" +
                    "or invp.facilitador like %?1% \n" +
                    "or invp.codigogenerado like %?1%\n" +
                    "or cyv.fechainicio like %?1% \n" +
                    "or cyv.numeropedido like %?1% ",
            nativeQuery = true)
    Page<Inventarioproducto> buscadorInventarioPrincipal(String search, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value= "UPDATE inventarioproducto SET cantidad = :cantidad WHERE (idinventario = :idinventario);", nativeQuery = true)
    void ActualizarCantidadInventarioPrincipal(@Param("cantidad") int cantidad, @Param("idinventario") int idinventario);

    //public Inventarioproducto findByProducto(Producto producto);







}
