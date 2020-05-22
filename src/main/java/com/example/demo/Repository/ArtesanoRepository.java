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

    List<Artesano> findByComunidad(int comunidad);

    List<Artesano> findByNombreartesano(String buscarArtesano);

    List<Artesano> findByNombreartesanoOrApellidomaternoOrApellidopaternoOrCodigoartesano(String nombre, String apellidoPa, String apellidoMa,String codigoArt);

    @Query(value="SELECT * FROM artesano where codigoartesano = ?1 and comunidad_idcomunidad=?2", nativeQuery =true)
    Artesano buscarCodigoartesanoyComunidad(String codigoArtesano, int idcomunidad);
}
