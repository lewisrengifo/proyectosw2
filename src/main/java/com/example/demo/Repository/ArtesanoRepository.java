package com.example.demo.Repository;

import com.example.demo.Entity.Artesano;
import com.example.demo.Entity.Comunidad;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtesanoRepository extends JpaRepository<Artesano,Integer> {


    @Query(value = "select * from artesano where comunidad_idcomunidad=?1", nativeQuery = true)
    List<Artesano> filtarPorComunidad(int idcomunidad);

    @Query(value="SELECT * FROM artesano a where a.nombreartesano like %?1% or a.apellidopaterno like %?1% " +
            " or a.apellidomaterno like %?1% or a.codigoartesano like %?1% or " +
            "a.comunidad_idcomunidad= (select idcomunidad from comunidad where nombrecomunidad like %?1%)",
            countQuery ="SELECT count(*) FROM artesano a where a.nombreartesano like %?1% or a.apellidopaterno like %?1% " +
                    "or a.apellidomaterno like %?1% or a.codigoartesano like %?1% or " +
                    "a.comunidad_idcomunidad= " +
                    "(select idcomunidad from comunidad where nombrecomunidad like %?1%)",
            nativeQuery = true)
    Page<Artesano> buscadorArtesano(String search, Pageable pageable);


    @Query(value="SELECT * FROM artesano where codigoartesano = ?1",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = ?1);"
            ,nativeQuery = true)
    List<Artesano> buscarSucomunidad(String codigo);

    @Query(value = "SELECT * FROM artesano where codigoartesano = ?1", nativeQuery=true)
    Artesano editarArtesanoBuscarCodigo(String codigo);


}
