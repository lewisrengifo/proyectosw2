package com.example.demo.Repository;

import com.example.demo.Entity.Comunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad,Integer> {
    @Query(value = "SELECT * FROM comunidad where comunidad.nombrecomunidad=?1 or comunidad.codigocomunidad=?1"
            , nativeQuery = true)
    List<Comunidad> filtro(String nom);
    @Query(value = "SELECT * FROM comunidad where idcomunidad not in (Select c.idcomunidad from comunidad c where c.idcomunidad=?1);"
            , nativeQuery = true)
    List<Comunidad> mio(int id);
}