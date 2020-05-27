package com.example.demo.Repository;

import com.example.demo.Entity.Comunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad,Integer> {

    Comunidad findByIdcomunidad(String idcomunidad);
}