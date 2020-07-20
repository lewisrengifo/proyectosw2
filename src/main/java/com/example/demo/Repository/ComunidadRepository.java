package com.example.demo.Repository;

import com.example.demo.Entity.Comunidad;
import com.example.demo.Entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad,Integer> {

    Comunidad findByIdcomunidad(String idcomunidad);

    @Query(value = "SELECT * FROM comunidad where comunidad.nombrecomunidad like %?1% or comunidad.codigocomunidad like %?1%"
            , nativeQuery = true)
    Page<Comunidad> filtro(String nom, Pageable pageable);
    @Query(value = "SELECT * FROM comunidad where idcomunidad not in (Select c.idcomunidad from comunidad c where c.idcomunidad=?1);"
            , nativeQuery = true)
    List<Comunidad> mio(int id);
    @Query(value = "select * from comunidad", nativeQuery = true)
    Page<Comunidad> findPaginado(Pageable pageable);

}