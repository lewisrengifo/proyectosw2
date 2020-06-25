package com.example.demo.Repository;

import com.example.demo.Entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
}
