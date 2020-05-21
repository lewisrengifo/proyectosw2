package com.example.demo.Repository;

import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {


    @Query(value = "select pr.* from producto pr\n" +
            "            where pr.nombreproducto = ?1 or pr.codigodescripcionproducto= ?1\n" +
            "               or pr.codigoproducto= ?1 or pr.linea_idlinea = (select li.idlinea\n" +
            "            from linea li\n" +
            "            where li.nombrelinea = ?1)\n" +
            "            order by pr.idproducto",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = 'Tradicional');"
                        , nativeQuery = true)
    Page<Producto> obtenerFiltroProductoTradicional(String search , Pageable pageable);

    @Query(value = "select pr.* from producto pr\n" +
            "            where pr.nombreproducto = ?1 or pr.codigodescripcionproducto= ?1\n" +
            "               or pr.codigoproducto= ?1 or pr.linea_idlinea = (select li.idlinea\n" +
            "            from linea li\n" +
            "            where li.nombrelinea = ?1)\n" +
            "            order by pr.idproducto",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = 'Mosqoy');"
            , nativeQuery = true)
    Page<Producto> obtenerFiltroProductoMosqoy(String search , Pageable pageable);

    @Query(value = "select pr.* from producto pr\n" +
            "            where pr.nombreproducto = ?1 or pr.codigodescripcionproducto= ?1\n" +
            "               or pr.codigoproducto= ?1 or pr.linea_idlinea = (select li.idlinea\n" +
            "            from linea li\n" +
            "            where li.nombrelinea = ?1)\n" +
            "            order by pr.idproducto",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = 'Mosqoy');"
            , nativeQuery = true)
    Page<Producto> obtenerFiltroProductoFibras(String search , Pageable pageable);



}
