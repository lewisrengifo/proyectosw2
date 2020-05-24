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

    @Query(value="SELECT * FROM artesano a inner join comunidad c " +
            "on a.comunidad_idcomunidad=c.idcomunidad" +
            " where a.nombreartesano=?1 or a.apellidopaterno=?1 " +
            " or a.apellidomaterno=?1 or a.codigoartesano=?1 or c.nombrecomunidad=?1;",nativeQuery = true)
    List<Artesano> buscadorArtesano(String search);

    List<Artesano> findByNombreartesanoOrApellidomaternoOrApellidopaternoOrCodigoartesano(String nombre, String apellidoPa, String apellidoMa,String codigoArt);

    List<Artesano> findByCodigoartesano(String codigoArtesano);

    @Query(value="SELECT * FROM artesano where codigoartesano = ?1", nativeQuery = true)
    List<Artesano> buscarSucomunidad(String codigo);
}
