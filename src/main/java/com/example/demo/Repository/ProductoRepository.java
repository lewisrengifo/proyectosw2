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
            "            where pr.nombreproducto = ?1 or pr.codigodescripcionproducto= ?1 " +
            "               or pr.codigoproducto= ?1 or pr.linea_idlinea = (select li.idlinea " +
            "            from linea li\n" +
            "            where li.nombrelinea = ?1)\n" +
            "            order by pr.idproducto",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = ?1);"
                        , nativeQuery = true)
    Page<Producto> obtenerFiltroProducto(String search , Pageable pageable);

    @Query(value = "SELECT * FROM producto where idproducto not in (Select p.idproducto from producto p where p.idproducto=?1);"
            , nativeQuery = true)
    List<Producto> mio(int id);

}
