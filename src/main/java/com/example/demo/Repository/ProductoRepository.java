package com.example.demo.Repository;

import com.example.demo.Entity.Inventarioproducto;
import com.example.demo.Entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {


    @Query(value = "SELECT * FROM producto p where p.nombreproducto like %?1% or p.codigoproducto like %?1% or p.descripcionproducto like %?1%\n" +
            "                            or p.codigodescripcionproducto like %?1% or p.linea_idlinea= (select linea_idlinea from linea where nombrelinea like %?1%)",
            countQuery = "SELECT count(*)  From producto p where p.nombreproducto like %?1% or p.codigoproducto like %?1% or p.descripcionproducto like %?1%\n" +
                    "                              or p.codigodescripcionproducto like %?1% or p.linea_idlinea= (select linea_idlinea from linea where nombrelinea like %?1%);"
                        , nativeQuery = true)
    Page<Producto> obtenerFiltroProducto(String search , Pageable pageable);

    @Query(value = "SELECT * FROM producto where idproducto not in (Select p.idproducto from producto p where p.idproducto=?1);"
            , nativeQuery = true)
    List<Producto> mio(int id);


}
