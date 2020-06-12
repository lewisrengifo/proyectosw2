package com.example.demo.Repository;

import com.example.demo.Entity.Artesano;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtesanoRepository extends JpaRepository<Artesano,Integer> {


    @Query(value = "select * from artesano where comunidad_idcomunidad=?1", nativeQuery = true)
    List<Artesano> filtarPorComunidad(int idcomunidad);

    @Query(value="SELECT * FROM artesano a where a.nombreartesano=?1 or a.apellidopaterno=?1 " +
            " or a.apellidomaterno=?1 or a.codigoartesano=?1 or " +
            "a.comunidad_idcomunidad= (select idcomunidad from comunidad where nombrecomunidad=?1)",nativeQuery = true)
    List<Artesano> buscadorArtesano(String search);

    List<Artesano> findByCodigoartesano(String codigoArtesano);

    @Query(value="SELECT * FROM artesano where codigoartesano = ?1",
            countQuery = "SELECT count(*) FROM producto pro where pro.linea_idlinea = (select li.idlinea from linea li where li.nombrelinea = ?1);"
            ,nativeQuery = true)
    List<Artesano> buscarSucomunidad(String codigo);
}
