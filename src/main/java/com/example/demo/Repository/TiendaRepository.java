package com.example.demo.Repository;

import com.example.demo.Entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
    @Query(value = "SELECT * FROM tienda where sede_idrol=?1", nativeQuery = true)
    List<Tienda> listaTiendasPorSede(int id);
}
