package com.example.demo.Repository;

import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
    @Query(value = "SELECT * FROM tienda where sede_idsede=?1", nativeQuery = true)
    List<Tienda> listaTiendasPorSede(int id);




}
