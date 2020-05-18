package com.example.demo.Repository;

import com.example.demo.Entity.Artesano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtesanoRepository extends JpaRepository<Artesano,Integer> {


    @Query(value = "select * from artesano where nombreartesano = ?1",
            nativeQuery = true)
    List<Artesano> buscarArtPorCompName(String nombre);
}
