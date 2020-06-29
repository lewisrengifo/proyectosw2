package com.example.demo.Repository;


import com.example.demo.Entity.Inventariosede;
import com.example.demo.Entity.Inventariotienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioTiendaRepository extends JpaRepository<Inventariotienda,Integer> {
}
